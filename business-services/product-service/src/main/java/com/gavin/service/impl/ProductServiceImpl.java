package com.gavin.service.impl;

import com.gavin.entity.CategoryEntity;
import com.gavin.entity.ProductEntity;
import com.gavin.entity.ProductReservationEntity;
import com.gavin.exception.RecordNotFoundException;
import com.gavin.exception.StocksNotEnoughException;
import com.gavin.model.PageArgument;
import com.gavin.model.dto.order.ItemDto;
import com.gavin.model.dto.product.CreateProductDto;
import com.gavin.model.dto.product.ProductReservationDto;
import com.gavin.model.vo.product.ProductVo;
import com.gavin.repository.CategoryRepository;
import com.gavin.repository.PointRewardPlanRepository;
import com.gavin.repository.ProductRepository;
import com.gavin.repository.ProductReservationRepository;
import com.gavin.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ModelMapper modelMapper;

    private final CategoryRepository categoryRepository;

    private final ProductRepository productRepository;

    private final ProductReservationRepository productReservationRepository;

    private final PointRewardPlanRepository pointRewardPlanRepository;

    @Autowired
    public ProductServiceImpl(
            ModelMapper modelMapper,
            CategoryRepository categoryRepository,
            ProductRepository productRepository,
            ProductReservationRepository productReservationRepository,
            PointRewardPlanRepository pointRewardPlanRepository) {
        this.modelMapper = modelMapper;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.productReservationRepository = productReservationRepository;
        this.pointRewardPlanRepository = pointRewardPlanRepository;
    }

    @Override
    @Transactional
    public ProductVo createProduct(CreateProductDto _product) {
        String categoryId = _product.getCategoryId();
        CategoryEntity categoryEntity = Optional.ofNullable(categoryRepository.findOne(categoryId))
                .orElseThrow(() -> new RecordNotFoundException("category", categoryId));

        ProductEntity productEntity = modelMapper.map(_product, ProductEntity.class);
        productRepository.save(productEntity);

        ProductVo productVo = modelMapper.map(productEntity, ProductVo.class);
        productVo.setCategoryName(categoryEntity.getName());

        return productVo;
    }

    @Override
    public ProductVo findProductById(String _productId) {
        ProductEntity productEntity = Optional.ofNullable(productRepository.findOne(_productId))
                .orElseThrow(() -> new RecordNotFoundException("product", _productId));

        ProductVo productVo = modelMapper.map(productEntity, ProductVo.class);
        CategoryEntity categoryEntity = categoryRepository.findOne(productEntity.getCategoryId());
        productVo.setCategoryName(categoryEntity.getName());

        return productVo;
    }

    @Override
    public List<ProductVo> findProductByCategoryId(String _categoryId, PageArgument _pageArgument) {
        PageRequest pageRequest = new PageRequest(
                _pageArgument.getCurrentPage(),
                _pageArgument.getPageSize(),
                new Sort(Sort.Direction.ASC, "id")
        );

        CategoryEntity categoryEntity = Optional.ofNullable(categoryRepository.findOne(_categoryId))
                .orElseThrow(() -> new RecordNotFoundException("category", _categoryId));

        Page<ProductEntity> productEntities = productRepository.findByCategoryId(_categoryId, pageRequest);

        List<ProductVo> productVos = new ArrayList<>();
        productEntities.forEach(
                productEntity -> {
                    ProductVo productVo = modelMapper.map(productEntity, ProductVo.class);
                    productVo.setCategoryName(categoryEntity.getName());
                    productVos.add(productVo);
                }
        );

        _pageArgument.setTotalPages(productEntities.getTotalPages());
        _pageArgument.setTotalElements(productEntities.getTotalElements());

        return productVos;
    }

    @Override
    @Transactional
    public List<ProductReservationDto> reserveProducts(String _orderId, List<ItemDto> _items) {
        List<String> productIds = _items.stream().map(ItemDto::getProductId).collect(Collectors.toList());

        List<ProductEntity> productEntities = productRepository.findAll(productIds);
        Map<String, ProductEntity> productIdEntityMap = productEntities.stream().collect(
                Collectors.toMap(ProductEntity::getId, productEntity -> productEntity));

        List<ProductReservationDto> productReservationDtos = new ArrayList<>();

        _items.forEach(
                item -> {
                    String productId = item.getProductId();
                    ProductEntity productEntity = productIdEntityMap.get(productId);

                    // 订单中此商品的订购数超过库存。
                    if (item.getQuantity() > productEntity.getStocks()) {
                        log.warn("订单中商品{}的订购数：{}，库存数：{}。", productEntity.getName(), item.getQuantity(), productEntity.getStocks());
                        throw new StocksNotEnoughException(String.format("product %s is insufficient.", productEntity.getName()));
                    }

                    // 从该商品的库存中冻结与订单相应的数目。
                    productEntity.setStocks(productEntity.getStocks() - item.getQuantity());
                    productRepository.save(productEntity);

                    // 记录到预约信息表中。
                    ProductReservationEntity productReservationEntity = new ProductReservationEntity();
                    productReservationEntity.setOrderId(_orderId);
                    productReservationEntity.setProductId(item.getProductId());
                    productReservationEntity.setQuantity(item.getQuantity());
                    productReservationRepository.save(productReservationEntity);

                    ProductReservationDto productReservationDto = new ProductReservationDto();
                    productReservationDto.setProductId(productId);
                    productReservationDto.setPrice(productEntity.getPrice());
                    productReservationDto.setQuantity(item.getQuantity());

                    // 查找该商品是否有处于有效期内的返点比例设置。
                    pointRewardPlanRepository.findApplicablePlanByProductId(productId)
                            .ifPresent(pointRewardPlanEntity -> productReservationDto.setRatio(pointRewardPlanEntity.getRatio()));

                    productReservationDtos.add(productReservationDto);
                }
        );

        return productReservationDtos;
    }

    @Override
    @Transactional
    public void cancelReservation(String _orderId) {
        List<ProductReservationEntity> productReservationEntities = productReservationRepository.findByOrderId(_orderId);

        List<String> productIds = productReservationEntities.stream()
                .map(ProductReservationEntity::getProductId)
                .collect(Collectors.toList());

        List<ProductEntity> productEntities = productRepository.findAll(productIds);

        Map<String, Integer> itemQuantityMap = productReservationEntities.stream()
                .collect(Collectors.toMap(
                        ProductReservationEntity::getProductId,
                        ProductReservationEntity::getQuantity));

        productEntities.forEach(
                productEntity -> {
                    String productId = productEntity.getId();
                    productEntity.setStocks(productEntity.getStocks() + itemQuantityMap.getOrDefault(productId, 0));
                    productRepository.save(productEntity);
                }
        );

        productReservationRepository.delete(productReservationEntities);
    }

}
