package com.gavin.service.impl;

import com.gavin.client.address.AddressClient;
import com.gavin.client.point.PointClient;
import com.gavin.client.product.ProductClient;
import com.gavin.constants.ResponseCodeConstants;
import com.gavin.dto.DirectionDto;
import com.gavin.entity.ItemEntity;
import com.gavin.entity.OrderEntity;
import com.gavin.enums.MessageableEventStatusEnums;
import com.gavin.enums.OrderStatusEnums;
import com.gavin.event.ArrangeShipmentEvent;
import com.gavin.event.CancelReservationEvent;
import com.gavin.event.WaitingForPaymentEvent;
import com.gavin.exception.*;
import com.gavin.message.producer.ArrangeShipmentMessageProducer;
import com.gavin.message.producer.CancelReservationMessageProducer;
import com.gavin.message.producer.WaitingForPaymentMessageProducer;
import com.gavin.model.PageArgument;
import com.gavin.model.Response;
import com.gavin.model.dto.order.CreateOrderDto;
import com.gavin.model.dto.order.ItemDto;
import com.gavin.model.dto.point.FreezePointsDto;
import com.gavin.model.dto.product.ProductReservationDto;
import com.gavin.model.vo.address.AddressVo;
import com.gavin.model.vo.order.OrderDetailsVo;
import com.gavin.model.vo.order.OrderVo;
import com.gavin.repository.OrderRepository;
import com.gavin.service.EventService;
import com.gavin.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final ModelMapper modelMapper;

    private final AddressClient addressClient;

    private final ProductClient productClient;

    private final PointClient pointClient;

    private final CancelReservationMessageProducer cancelReservationMessageProducer;

    private final WaitingForPaymentMessageProducer waitingForPaymentMessageProducer;

    private final ArrangeShipmentMessageProducer arrangeShipmentMessageProducer;

    private final EventService<CancelReservationEvent> cancelReservationEventService;

    private final EventService<ArrangeShipmentEvent> arrangeShipmentEventService;

    private final EventService<WaitingForPaymentEvent> waitingForPaymentEventService;

    private final OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(
            ModelMapper modelMapper,
            AddressClient addressClient,
            ProductClient productClient,
            PointClient pointClient,
            CancelReservationMessageProducer cancelReservationMessageProducer,
            WaitingForPaymentMessageProducer waitingForPaymentMessageProducer,
            ArrangeShipmentMessageProducer arrangeShipmentMessageProducer,
            EventService<CancelReservationEvent> cancelReservationEventService,
            EventService<ArrangeShipmentEvent> arrangeShipmentEventService,
            EventService<WaitingForPaymentEvent> waitingForPaymentEventService,
            OrderRepository orderRepository) {
        this.modelMapper = modelMapper;
        this.addressClient = addressClient;
        this.productClient = productClient;
        this.pointClient = pointClient;
        this.cancelReservationMessageProducer = cancelReservationMessageProducer;
        this.waitingForPaymentMessageProducer = waitingForPaymentMessageProducer;
        this.arrangeShipmentMessageProducer = arrangeShipmentMessageProducer;
        this.cancelReservationEventService = cancelReservationEventService;
        this.arrangeShipmentEventService = arrangeShipmentEventService;
        this.waitingForPaymentEventService = waitingForPaymentEventService;
        this.orderRepository = orderRepository;
    }

    @Override
    public OrderDetailsVo createOrder(CreateOrderDto _order) {
        // 获得收件人姓名和地址。
        DirectionDto direction = getRecipientDirection(_order.getAddressId());
        // 创建临时订单。
        String orderId = createProvisionalOrder(_order.getUserId());

        List<ProductReservationDto> reservedItems;
        try {
            // 调用商品服务，要求保留订单中的商品。
            reservedItems = reserveProducts(orderId, _order.getItems());
            updateOrderStatus(orderId, OrderStatusEnums.RESERVED);
        } catch (Exception e) {
            orderRepository.delete(orderId);
            throw new OrderCreationException(e);
        }

        // 补全订单信息。
        try {
            return supplementOrderInformation(orderId, direction, reservedItems);
        } catch (Exception e) {
            supplementOrderInformationFallback(orderId);
            throw new OrderCreationException(e);
        }
    }

    /**
     * 为了取得订单ID而创建一个临时订单。
     *
     * @return 创建的订单ID
     */
    private String createProvisionalOrder(String _userId) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setUserId(_userId);
        orderEntity.setStatus(OrderStatusEnums.CREATED);

        orderRepository.save(orderEntity);
        return orderEntity.getId();
    }

    /**
     * 根据地址ID获得收件人姓名和地址。
     *
     * @param _addressId 地址ID
     * @return 收件人姓名和地址
     */
    private DirectionDto getRecipientDirection(String _addressId) {
        // 调用地址服务查询收件人详细地址。
        Response<AddressVo> addressResponse = addressClient.findAddressById(_addressId);
        if (!ResponseCodeConstants.SUCCESS.equals(addressResponse.getCode())) {
            throw new AddressFetchException();
        }
        AddressVo addressVo = addressResponse.getContents();

        return modelMapper.map(addressVo, DirectionDto.class);
    }

    /**
     * 保留订单中指定的商品。
     *
     * @param _orderId 订单ID
     * @param _items   要保留的商品
     * @return 保留成功的商品信息
     */
    private List<ProductReservationDto> reserveProducts(String _orderId, List<ItemDto> _items) {
        // 调用商品服务尝试锁定库存。
        Response<List<ProductReservationDto>> reservationResponse = productClient.reserveProducts(_orderId, _items);

        if (ResponseCodeConstants.SUCCESS.equals(reservationResponse.getCode())) {
            String productIds = _items.stream()
                    .map(ItemDto::getProductId)
                    .collect(Collectors.joining(", "));
            log.info("调用商品服务保留订单中商品{}成功。", productIds);
        } else {
            log.error("调用商品服务保留订单中商品失败。");
            throw new ProductsReserveException();
        }
        return reservationResponse.getContents();
    }

    /**
     * 补全订单信息。
     *
     * @param _orderId       订单ID
     * @param _direction     收件人地址和姓名
     * @param _reservedItems 保留成功的商品列表
     * @return 订单消息信息
     */
    @Transactional
    private OrderDetailsVo supplementOrderInformation(String _orderId,
                                                      DirectionDto _direction,
                                                      List<ProductReservationDto> _reservedItems) {
        OrderEntity orderEntity = Optional.ofNullable(orderRepository.findOne(_orderId))
                .orElseThrow(() -> new RecordNotFoundException("order", _orderId));

        orderEntity.setConsignee(_direction.getConsignee());
        orderEntity.setAddress(_direction.getAddress());
        orderEntity.setPhoneNumber(_direction.getPhoneNumber());

        BigDecimal totalAmountPerOrder = new BigDecimal("0");
        BigDecimal totalPointsPerOrder = new BigDecimal("0");
        for (ProductReservationDto reservedItem : _reservedItems) {
            ItemEntity itemEntity = modelMapper.map(reservedItem, ItemEntity.class);
            orderEntity.addItemEntity(itemEntity);

            // 计算该种商品的总金额。
            BigDecimal totalAmountPerItem = new BigDecimal(reservedItem.getPrice() * reservedItem.getQuantity());

            // 计算该种商品可获得的积分数。
            BigDecimal totalPointsPerItem = new BigDecimal("0");
            if (null != reservedItem.getRatio()) {
                totalPointsPerItem = totalAmountPerItem.multiply(new BigDecimal(reservedItem.getRatio())).setScale(0, BigDecimal.ROUND_HALF_UP);
            }

            totalAmountPerOrder = totalAmountPerOrder.add(totalAmountPerItem);
            totalPointsPerOrder = totalPointsPerOrder.add(totalPointsPerItem);
        }

        orderEntity.setTotalAmount(totalAmountPerOrder);
        orderEntity.setRewardPoints(totalPointsPerOrder);
        orderEntity.setStatus(OrderStatusEnums.RESERVED);
        orderRepository.save(orderEntity);

        return modelMapper.map(orderEntity, OrderDetailsVo.class);
    }

    /**
     * 补全订单信息失败时的处理。
     *
     * @param _orderId 订单ID
     */
    @Transactional
    private void supplementOrderInformationFallback(String _orderId) {
        CancelReservationEvent event = new CancelReservationEvent();
        event.setOrderId(_orderId);
        cancelReservationEventService.saveEvent(event, MessageableEventStatusEnums.NEW);

        updateOrderStatus(_orderId, OrderStatusEnums.ERROR);
    }

    @Override
    public OrderDetailsVo findOrderById(String _orderId) {
        OrderEntity orderEntity = Optional.ofNullable(orderRepository.findOne(_orderId))
                .orElseThrow(() -> new RecordNotFoundException("order", _orderId));

        return modelMapper.map(orderEntity, OrderDetailsVo.class);
    }

    @Override
    public List<OrderVo> findOrdersByUserId(String _userId, PageArgument _pageArgument) {
        PageRequest pageRequest = new PageRequest(
                _pageArgument.getCurrentPage(),
                _pageArgument.getPageSize(),
                new Sort(Sort.Direction.ASC, "id"));

        Page<OrderEntity> orderEntities = orderRepository.findByUserId(_userId, pageRequest);

        List<OrderVo> orderVos = new ArrayList<>();
        orderEntities.forEach(
                orderEntity -> {
                    OrderVo orderVo = modelMapper.map(orderEntity, OrderVo.class);
                    orderVos.add(orderVo);
                }
        );

        _pageArgument.setTotalPages(orderEntities.getTotalPages());
        _pageArgument.setTotalElements(orderEntities.getTotalElements());

        return orderVos;
    }

    @Override
    public DirectionDto findDirectionByOrderId(String _orderId) {
        OrderEntity orderEntity = Optional.ofNullable(orderRepository.findOne(_orderId))
                .orElseThrow(() -> new RecordNotFoundException("order", _orderId));

        return modelMapper.map(orderEntity, DirectionDto.class);
    }

    @Override
    public void updateOrderStatus(String _orderId, OrderStatusEnums _status) {
        OrderEntity orderEntity = Optional.ofNullable(orderRepository.findOne(_orderId))
                .orElseThrow(() -> new RecordNotFoundException("order", _orderId));

        orderEntity.setStatus(_status);
        orderRepository.save(orderEntity);
    }

    @Override
    @Transactional
    public void cancelOrder(String _orderId) {
        OrderEntity orderEntity = Optional.ofNullable(orderRepository.findOne(_orderId))
                .orElseThrow(() -> new RecordNotFoundException("order", _orderId));

        OrderStatusEnums[] cancelableOrderStatus = new OrderStatusEnums[]{
                OrderStatusEnums.CREATED,
                OrderStatusEnums.RESERVED};

        if (!Arrays.asList(cancelableOrderStatus).contains(orderEntity.getStatus())) {
            throw new CannotCancelOrderException();
        }

        CancelReservationEvent event = new CancelReservationEvent();
        event.setOrderId(_orderId);
        cancelReservationEventService.saveEvent(event, MessageableEventStatusEnums.NEW);

        updateOrderStatus(_orderId, OrderStatusEnums.CANCELED);
    }

    @Override
    @Transactional
    public void payOrder(String _orderId, BigDecimal _pointsAmount) {
        OrderEntity orderEntity = Optional.ofNullable(orderRepository.findOne(_orderId))
                .orElseThrow(() -> new RecordNotFoundException("order", _orderId));

        // 用户选择使用积分抵扣。
        if (null != _pointsAmount && !_pointsAmount.equals(new BigDecimal("0"))) {
            FreezePointsDto freezePointsDto = new FreezePointsDto();
            freezePointsDto.setUserId(orderEntity.getUserId());
            freezePointsDto.setOrderId(_orderId);
            freezePointsDto.setAmount(_pointsAmount);

            // 调用积分服务冻结积分。
            Response response = pointClient.freezePoints(freezePointsDto);
            if (!ResponseCodeConstants.SUCCESS.equals(response.getCode())) {
                log.warn("调用积分服务冻结积分失败。");
                throw new PointsFreezeException();
            }
        } else {
            _pointsAmount = new BigDecimal("0");
        }

        // 计算用积分抵扣后还需要支付的金额。
        BigDecimal payWithMoneyAmount = orderEntity.getTotalAmount().subtract(_pointsAmount);

        // 订单金额全部用积分抵扣，无需另外支付。
        if (payWithMoneyAmount.intValue() <= 0) {
            log.info("订单{}的费用已全部用积分支付。", _orderId);

            // 发送消息至物流服务。
            succeedInPayment(_orderId);
        } else {
            // 发送消息至支付服务。
            startWaitingForPayment(orderEntity, payWithMoneyAmount);
        }
    }

    @Override
    @Transactional
    public void succeedInPayment(String _orderId) {
        OrderEntity orderEntity = Optional.ofNullable(orderRepository.findOne(_orderId))
                .orElseThrow(() -> new RecordNotFoundException("order", _orderId));

        ArrangeShipmentEvent event = modelMapper.map(orderEntity, ArrangeShipmentEvent.class);
        event.setOrderId(_orderId);

        orderEntity.setStatus(OrderStatusEnums.PAID);
        orderRepository.save(orderEntity);

        arrangeShipmentEventService.saveEvent(event, MessageableEventStatusEnums.NEW);
    }

    @Transactional
    private void startWaitingForPayment(OrderEntity _entity, BigDecimal _amount) {
        WaitingForPaymentEvent event = new WaitingForPaymentEvent();
        event.setUserId(_entity.getUserId());
        event.setOrderId(_entity.getId());
        event.setAmount(_amount);

        _entity.setStatus(OrderStatusEnums.PAYING);
        orderRepository.save(_entity);

        waitingForPaymentEventService.saveEvent(event, MessageableEventStatusEnums.NEW);
    }

    @Override
    @Transactional
    public boolean publishCancelReservationEvent(CancelReservationEvent _event) {
        cancelReservationEventService.updateEventStatusById(
                _event.getOriginId(),
                MessageableEventStatusEnums.PUBLISHED);

        return cancelReservationMessageProducer.sendMessage(_event);
    }

    @Override
    @Transactional
    public boolean publishArrangeShipmentEvent(ArrangeShipmentEvent _event) {
        arrangeShipmentEventService.updateEventStatusById(
                _event.getOriginId(),
                MessageableEventStatusEnums.PUBLISHED);

        return arrangeShipmentMessageProducer.sendMessage(_event);
    }

    @Override
    @Transactional
    public boolean publishWaitingForPaymentEvent(WaitingForPaymentEvent _event) {
        waitingForPaymentEventService.updateEventStatusById(
                _event.getOriginId(),
                MessageableEventStatusEnums.PUBLISHED);

        return waitingForPaymentMessageProducer.sendMessage(_event);
    }

}
