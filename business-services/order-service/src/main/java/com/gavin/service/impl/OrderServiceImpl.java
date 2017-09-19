package com.gavin.service.impl;

import com.gavin.client.address.AddressClient;
import com.gavin.client.point.PointClient;
import com.gavin.client.product.ProductClient;
import com.gavin.constants.ResponseCodeConstants;
import com.gavin.domain.Item;
import com.gavin.domain.Order;
import com.gavin.dto.DirectionDto;
import com.gavin.enums.OrderStatusEnums;
import com.gavin.exception.*;
import com.gavin.messaging.ArrangeShipmentProcessor;
import com.gavin.messaging.CancelReservationProcessor;
import com.gavin.messaging.WaitingForPaymentProcessor;
import com.gavin.model.CustomResponseBody;
import com.gavin.model.PageResult;
import com.gavin.model.dto.address.AddressDto;
import com.gavin.model.dto.order.CreateOrderDto;
import com.gavin.model.dto.order.ItemDto;
import com.gavin.model.dto.order.OrderDetailsDto;
import com.gavin.model.dto.order.OrderDto;
import com.gavin.model.dto.point.FreezePointsDto;
import com.gavin.model.dto.product.ReservedProductDto;
import com.gavin.payload.ArrangeShipmentPayload;
import com.gavin.payload.CancelReservationPayload;
import com.gavin.payload.WaitingForPaymentPayload;
import com.gavin.repository.OrderRepository;
import com.gavin.service.OrderService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AddressClient addressClient;

    @Autowired
    private ProductClient productClient;

    @Autowired
    private PointClient pointClient;

    @Autowired
    private ArrangeShipmentProcessor arrangeShipmentProcessor;

    @Autowired
    private CancelReservationProcessor cancelReservationProcessor;

    @Autowired
    private WaitingForPaymentProcessor waitingForPaymentProcessor;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public OrderDetailsDto createOrder(String _userId, CreateOrderDto _order) {
        OrderDetailsDto orderDetailsDto = new OrderBuilder()
                .withUserId(_userId)
                .withAddressId(_order.getAddressId())
                .withItems(_order.getItems())
                .build();

        log.info("create order successfully. {}", new Gson().toJson(orderDetailsDto));
        return orderDetailsDto;
    }

    @Override
    public OrderDetailsDto findOrderById(String _orderId) {
        Order order = Optional.ofNullable(orderRepository.findOne(_orderId))
                .orElseThrow(() -> new RecordNotFoundException("order", _orderId));

        return modelMapper.map(order, OrderDetailsDto.class);
    }

    @Override
    public PageResult<OrderDto> findOrdersByUserId(String _userId, PageRequest _pageRequest) {
        Page<Order> orderEntities = orderRepository.findByUserId(_userId, _pageRequest);

        List<OrderDto> orderDtos = new ArrayList<>();
        orderEntities.forEach(
                order -> {
                    OrderDto orderDto = modelMapper.map(order, OrderDto.class);
                    orderDtos.add(orderDto);
                }
        );

        PageResult<OrderDto> pageResult = new PageResult<>();
        pageResult.setRecords(orderDtos);
        pageResult.setTotalPages(orderEntities.getTotalPages());
        pageResult.setTotalElements(orderEntities.getTotalElements());

        return pageResult;
    }

    @Override
    public DirectionDto findDirectionByOrderId(String _orderId) {
        Order order = Optional.ofNullable(orderRepository.findOne(_orderId))
                .orElseThrow(() -> new RecordNotFoundException("order", _orderId));

        return modelMapper.map(order, DirectionDto.class);
    }

    @Override
    public void updateOrderStatus(String _orderId, OrderStatusEnums _status) {
        Order order = Optional.ofNullable(orderRepository.findOne(_orderId))
                .orElseThrow(() -> new RecordNotFoundException("order", _orderId));

        order.setStatus(_status);
        orderRepository.save(order);
    }

    @Override
    @Transactional
    public void cancelOrder(String _orderId) {
        Order order = Optional.ofNullable(orderRepository.findOne(_orderId))
                .orElseThrow(() -> new RecordNotFoundException("order", _orderId));

        OrderStatusEnums[] cancelableOrderStatus = new OrderStatusEnums[]{
                OrderStatusEnums.CREATED,
                OrderStatusEnums.RESERVED};

        if (!Arrays.asList(cancelableOrderStatus).contains(order.getStatus())) {
            throw new OrderCancelException(String.format("can not cancel order(%s) because of order's status(%s).", _orderId, order.getStatus()));
        }

        CancelReservationPayload payload = new CancelReservationPayload();
        payload.setOrderId(_orderId);
        Message<CancelReservationPayload> message = MessageBuilder.withPayload(payload).build();
        cancelReservationProcessor.output().send(message);

        updateOrderStatus(_orderId, OrderStatusEnums.CANCELED);
        log.info("cancel order({}) successfully. ", _orderId);
    }

    @Override
    @Transactional
    public void payOrder(String _orderId, BigDecimal _pointsAmount) {
        Order order = Optional.ofNullable(orderRepository.findOne(_orderId))
                .orElseThrow(() -> new RecordNotFoundException("order", _orderId));

        // 用户选择使用积分抵扣。
        if (null != _pointsAmount && !_pointsAmount.equals(new BigDecimal("0"))) {
            FreezePointsDto freezePointsDto = new FreezePointsDto();
            freezePointsDto.setUserId(order.getUserId());
            freezePointsDto.setOrderId(_orderId);
            freezePointsDto.setAmount(_pointsAmount);

            // 调用积分服务冻结积分。
            CustomResponseBody responseBody = pointClient.freezePoints(freezePointsDto);
            if (!ResponseCodeConstants.OK.equals(responseBody.getResultCode())) {
                log.warn("call point-service to freeze points failed.");
                throw new PointsFreezeException("freeze points failed.");
            }
        } else {
            _pointsAmount = new BigDecimal("0");
        }

        // 计算用积分抵扣后还需要支付的金额。
        BigDecimal payWithMoneyAmount = order.getTotalAmount().subtract(_pointsAmount);

        // 订单金额全部用积分抵扣，无需另外支付。
        if (payWithMoneyAmount.intValue() <= 0) {
            log.info("pay order({}) all with points.", _orderId);

            this.succeedInPayment(_orderId);
        } else {
            order.setStatus(OrderStatusEnums.PAYING);
            orderRepository.save(order);

            // 发送消息至payment-service。
            WaitingForPaymentPayload payload = new WaitingForPaymentPayload();
            payload.setUserId(order.getUserId());
            payload.setOrderId(_orderId);
            payload.setAmount(payWithMoneyAmount);
            Message<WaitingForPaymentPayload> message = MessageBuilder.withPayload(payload).build();
            waitingForPaymentProcessor.output().send(message);
        }
    }

    @Override
    @Transactional
    public void succeedInPayment(String _orderId) {
        Order order = Optional.ofNullable(orderRepository.findOne(_orderId))
                .orElseThrow(() -> new RecordNotFoundException("order", _orderId));

        order.setStatus(OrderStatusEnums.PAID);
        orderRepository.save(order);

        // 发送消息至delivery-service。
        ArrangeShipmentPayload payload = modelMapper.map(order, ArrangeShipmentPayload.class);
        payload.setOrderId(order.getId());
        Message<ArrangeShipmentPayload> message = MessageBuilder.withPayload(payload).build();
        arrangeShipmentProcessor.output().send(message);
    }

    private class OrderBuilder {

        private String orderId;

        private DirectionDto direction;

        private List<ReservedProductDto> reservedProducts;

        OrderBuilder withUserId(String _userId) {
            Order order = new Order();
            order.setUserId(_userId);
            order.setStatus(OrderStatusEnums.CREATED);
            orderRepository.save(order);

            this.orderId = order.getId();
            return this;
        }

        OrderBuilder withAddressId(String _addressId) {
            Assert.notNull(orderId, "'withUserId' method must be called previously.");
            try {
                this.direction = getRecipientDirection(_addressId);
                return this;
            } catch (AddressFetchException e) {
                updateOrderStatus(orderId, OrderStatusEnums.ERROR);
                throw e;
            }
        }

        OrderBuilder withItems(List<ItemDto> _items) {
            Assert.notNull(orderId, "'withUserId' method must be called previously.");
            try {
                this.reservedProducts = reserveProducts(orderId, _items);
                return this;
            } catch (ProductsReserveException e) {
                updateOrderStatus(orderId, OrderStatusEnums.ERROR);
                throw e;
            }
        }

        /**
         * 根据地址ID获得收件人姓名和地址。
         *
         * @param _addressId 地址ID
         * @return 收件人姓名和地址
         */
        private DirectionDto getRecipientDirection(String _addressId) {
            // 调用地址服务查询收件人详细地址。
            CustomResponseBody<AddressDto> addressResponse;
            if (StringUtils.isNotBlank(_addressId)) {
                addressResponse = addressClient.findAddressById(_addressId);
            } else {
                addressResponse = addressClient.findDefaultAddress();
            }

            if (!ResponseCodeConstants.OK.equals(addressResponse.getResultCode())) {
                throw new AddressFetchException(String.format("can not fetch address(%s)", _addressId));
            }
            AddressDto addressDto = addressResponse.getContents();

            return modelMapper.map(addressDto, DirectionDto.class);
        }

        /**
         * 保留订单中指定的商品。
         *
         * @param _orderId 订单ID
         * @param _items   要保留的商品
         * @return 保留成功的商品信息
         */
        private List<ReservedProductDto> reserveProducts(String _orderId, List<ItemDto> _items) {
            // 调用商品服务尝试锁定库存。
            CustomResponseBody<List<ReservedProductDto>> reservationResponse = productClient.reserveProducts(_orderId, _items);

            if (ResponseCodeConstants.OK.equals(reservationResponse.getResultCode())) {
                String productIds = _items.stream()
                        .map(ItemDto::getProductId)
                        .collect(Collectors.joining(", "));
                log.info("reserve products successfully. {}", productIds);
            } else {
                log.error("reserve products failed.");
                throw new ProductsReserveException("can not reserve products");
            }
            return reservationResponse.getContents();
        }

        @Transactional
        private OrderDetailsDto build() {
            Order order = Optional.ofNullable(orderRepository.findOne(orderId))
                    .orElseThrow(() -> new RecordNotFoundException("order", orderId));

            order.setConsignee(direction.getConsignee());
            order.setAddress(direction.getAddress());
            order.setPhoneNumber(direction.getPhoneNumber());

            BigDecimal totalAmountPerOrder = new BigDecimal("0");
            BigDecimal totalPointsPerOrder = new BigDecimal("0");

            for (ReservedProductDto reservedProduct : reservedProducts) {
                Item item = modelMapper.map(reservedProduct, Item.class);
                order.addItem(item);

                // 计算该种商品的总金额。
                BigDecimal totalAmountPerItem = new BigDecimal(reservedProduct.getPrice() * reservedProduct.getQuantity());

                // 计算该种商品可获得的积分数。
                BigDecimal totalPointsPerItem = new BigDecimal("0");
                if (null != reservedProduct.getRatio()) {
                    totalPointsPerItem = totalAmountPerItem.multiply(new BigDecimal(reservedProduct.getRatio())).setScale(0, BigDecimal.ROUND_HALF_UP);
                }

                totalAmountPerOrder = totalAmountPerOrder.add(totalAmountPerItem);
                totalPointsPerOrder = totalPointsPerOrder.add(totalPointsPerItem);
            }

            order.setTotalAmount(totalAmountPerOrder);
            order.setRewardPoints(totalPointsPerOrder);
            order.setStatus(OrderStatusEnums.RESERVED);
            orderRepository.save(order);

            return modelMapper.map(order, OrderDetailsDto.class);
        }

    }

}
