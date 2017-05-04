package com.gavin.service.impl;

import com.gavin.entity.CategoryEntity;
import com.gavin.entity.ProductEntity;
import com.gavin.entity.ProductReservationEntity;
import com.gavin.exception.InsufficientInventoryException;
import com.gavin.exception.RecordNotFoundException;
import com.gavin.model.PageResult;
import com.gavin.model.dto.order.ItemDto;
import com.gavin.model.dto.product.CreateProductDto;
import com.gavin.model.dto.product.ProductDto;
import com.gavin.model.dto.product.ReservedProductDto;
import com.gavin.repository.CategoryRepository;
import com.gavin.repository.PointRewardPlanRepository;
import com.gavin.repository.ProductRepository;
import com.gavin.repository.ProductReservationRepository;
import com.gavin.service.ProductService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    public ProductDto createProduct(CreateProductDto _product) {
        String categoryId = _product.getCategoryId();
        CategoryEntity categoryEntity = Optional.ofNullable(categoryRepository.findOne(categoryId))
                .orElseThrow(() -> new RecordNotFoundException("category", categoryId));

        ProductEntity productEntity = modelMapper.map(_product, ProductEntity.class);
        productRepository.save(productEntity);

        ProductDto productDto = modelMapper.map(productEntity, ProductDto.class);
        productDto.setCategoryName(categoryEntity.getName());

        log.info("create product successfully. {}", new Gson().toJson(productDto));

        return productDto;
    }

    @Override
    public ProductDto findProductById(String _productId) {
        ProductEntity productEntity = Optional.ofNullable(productRepository.findOne(_productId))
                .orElseThrow(() -> new RecordNotFoundException("product", _productId));

        ProductDto productDto = modelMapper.map(productEntity, ProductDto.class);
        CategoryEntity categoryEntity = categoryRepository.findOne(productEntity.getCategoryId());
        productDto.setCategoryName(categoryEntity.getName());

        return productDto;
    }

    @Override
    public PageResult<ProductDto> findProductByCategoryId(String _categoryId, PageRequest _pageRequest) {
        CategoryEntity categoryEntity = Optional.ofNullable(categoryRepository.findOne(_categoryId))
                .orElseThrow(() -> new RecordNotFoundException("category", _categoryId));

        Page<ProductEntity> productEntities = productRepository.findByCategoryId(_categoryId, _pageRequest);
        List<ProductDto> productDtos = new ArrayList<>();
        productEntities.forEach(
                productEntity -> {
                    ProductDto productDto = modelMapper.map(productEntity, ProductDto.class);
                    productDto.setCategoryName(categoryEntity.getName());
                    productDtos.add(productDto);
                }
        );

        PageResult<ProductDto> pageResult = new PageResult<>();
        pageResult.setRecords(productDtos);
        pageResult.setTotalPages(productEntities.getTotalPages());
        pageResult.setTotalElements(productEntities.getTotalElements());

        return pageResult;
    }

    @Override
    @Transactional
    public List<ReservedProductDto> reserveProducts(String _orderId, List<ItemDto> _items) {
        List<String> productIds = _items.stream().map(ItemDto::getProductId).collect(Collectors.toList());

        List<ProductEntity> productEntities = productRepository.findAll(productIds);
        Map<String, ProductEntity> productIdEntityMap = productEntities.stream().collect(
                Collectors.toMap(ProductEntity::getId, productEntity -> productEntity));

        List<ReservedProductDto> reservedProductDtos = new ArrayList<>();

        _items.forEach(
                item -> {
                    String productId = item.getProductId();
                    ProductEntity productEntity = productIdEntityMap.get(productId);

                    // 订单中此商品的订购数超过库存。
                    if (item.getQuantity() > productEntity.getStocks()) {
                        log.warn("{} of product({}) on order, but {} in stock.", item.getQuantity(), productEntity.getName(), productEntity.getStocks());
                        throw new InsufficientInventoryException(String.format("product %s is insufficient.", productEntity.getName()));
                    }

                    // 从该商品的库存中冻结与订单相应的数目。
//                    productEntity.setStocks(productEntity.getStocks() - item.getQuantity());
                    productRepository.save(productEntity);

                    // 记录到预约信息表中。
                    ProductReservationEntity productReservationEntity = new ProductReservationEntity();
                    productReservationEntity.setOrderId(_orderId);
                    productReservationEntity.setProductId(item.getProductId());
                    productReservationEntity.setQuantity(item.getQuantity());
                    productReservationRepository.save(productReservationEntity);

                    ReservedProductDto reservedProductDto = new ReservedProductDto();
                    reservedProductDto.setProductId(productId);
                    reservedProductDto.setPrice(productEntity.getPrice());
                    reservedProductDto.setQuantity(item.getQuantity());

                    // 查找该商品是否有处于有效期内的返点比例设置。
                    pointRewardPlanRepository.findApplicablePlanByProductId(productId)
                            .ifPresent(pointRewardPlanEntity -> reservedProductDto.setRatio(pointRewardPlanEntity.getRatio()));

                    reservedProductDtos.add(reservedProductDto);
                }
        );

        log.info("reserve products successfully. {}", new Gson().toJson(reservedProductDtos));

        return reservedProductDtos;
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
