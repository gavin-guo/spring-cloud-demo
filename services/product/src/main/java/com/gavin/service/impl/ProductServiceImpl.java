package com.gavin.service.impl;

import com.gavin.domain.Category;
import com.gavin.domain.Product;
import com.gavin.domain.ProductReservation;
import com.gavin.dto.common.PageResult;
import com.gavin.dto.order.ItemDto;
import com.gavin.dto.product.CreateProductDto;
import com.gavin.dto.product.ProductDto;
import com.gavin.dto.product.ReservedProductDto;
import com.gavin.exception.InsufficientInventoryException;
import com.gavin.exception.RecordNotFoundException;
import com.gavin.repository.jpa.CategoryRepository;
import com.gavin.repository.jpa.PointRewardPlanRepository;
import com.gavin.repository.jpa.ProductRepository;
import com.gavin.repository.jpa.ProductReservationRepository;
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

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductReservationRepository productReservationRepository;

    @Autowired
    private PointRewardPlanRepository pointRewardPlanRepository;

    @Override
    @Transactional
    public ProductDto createProduct(CreateProductDto _product) {
        String categoryId = _product.getCategoryId();
        Category category = Optional.ofNullable(categoryRepository.findOne(categoryId))
                .orElseThrow(() -> new RecordNotFoundException("category", categoryId));

        Product product = modelMapper.map(_product, Product.class);
        productRepository.save(product);

        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        productDto.setCategoryName(category.getName());

        log.info("create product successfully. {}", new Gson().toJson(productDto));

        return productDto;
    }

    @Override
    public ProductDto findProductById(String _productId) {
        Product product = Optional.ofNullable(productRepository.findOne(_productId))
                .orElseThrow(() -> new RecordNotFoundException("product", _productId));

        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        Category category = categoryRepository.findOne(product.getCategoryId());
        productDto.setCategoryName(category.getName());

        return productDto;
    }

    @Override
    public PageResult<ProductDto> findProductByCategoryId(String _categoryId, PageRequest _pageRequest) {
        Category category = Optional.ofNullable(categoryRepository.findOne(_categoryId))
                .orElseThrow(() -> new RecordNotFoundException("category", _categoryId));

        Page<Product> products = productRepository.findByCategoryId(_categoryId, _pageRequest);
        List<ProductDto> productDtos = new ArrayList<>();
        products.forEach(
                product -> {
                    ProductDto productDto = modelMapper.map(product, ProductDto.class);
                    productDto.setCategoryName(category.getName());
                    productDtos.add(productDto);
                }
        );

        PageResult<ProductDto> pageResult = new PageResult<>();
        pageResult.setRecords(productDtos);
        pageResult.setTotalPages(products.getTotalPages());
        pageResult.setTotalRecords(products.getTotalElements());

        return pageResult;
    }

    @Override
    @Transactional
    public List<ReservedProductDto> reserveProducts(String _orderId, List<ItemDto> _items) {
        List<String> productIds = _items.stream().map(ItemDto::getProductId).collect(Collectors.toList());

        List<Product> products = productRepository.findAll(productIds);
        Map<String, Product> productIdMap = products.stream().collect(
                Collectors.toMap(Product::getId, product -> product));

        List<ReservedProductDto> reservedProductDtos = new ArrayList<>();

        _items.forEach(
                item -> {
                    String productId = item.getProductId();
                    Product product = productIdMap.get(productId);

                    // 订单中此商品的订购数超过库存。
                    if (item.getQuantity() > product.getStocks()) {
                        log.warn("{} of product({}) on order, but {} in stock.", item.getQuantity(), product.getName(), product.getStocks());
                        throw new InsufficientInventoryException(String.format("product %s is insufficient.", product.getName()));
                    }

                    // 从该商品的库存中冻结与订单相应的数目。
                    product.setStocks(product.getStocks() - item.getQuantity());
                    productRepository.save(product);

                    // 记录到预约信息表中。
                    ProductReservation productReservation = new ProductReservation();
                    productReservation.setOrderId(_orderId);
                    productReservation.setProductId(item.getProductId());
                    productReservation.setQuantity(item.getQuantity());
                    productReservationRepository.save(productReservation);

                    ReservedProductDto reservedProductDto = new ReservedProductDto();
                    reservedProductDto.setProductId(productId);
                    reservedProductDto.setPrice(product.getPrice());
                    reservedProductDto.setQuantity(item.getQuantity());

                    // 查找该商品是否有处于有效期内的返点比例设置。
                    pointRewardPlanRepository.findApplicablePlanByProductId(productId)
                            .ifPresent(pointRewardPlan -> reservedProductDto.setRatio(pointRewardPlan.getRatio()));

                    reservedProductDtos.add(reservedProductDto);
                }
        );

        log.info("reserve products successfully. {}", new Gson().toJson(reservedProductDtos));

        return reservedProductDtos;
    }

    @Override
    @Transactional
    public void cancelReservation(String _orderId) {
        List<ProductReservation> productReservations = productReservationRepository.findByOrderId(_orderId);

        List<String> productIds = productReservations.stream()
                .map(ProductReservation::getProductId)
                .collect(Collectors.toList());

        List<Product> products = productRepository.findAll(productIds);

        Map<String, Integer> itemQuantityMap = productReservations.stream()
                .collect(Collectors.toMap(
                        ProductReservation::getProductId,
                        ProductReservation::getQuantity));

        products.forEach(
                product -> {
                    String productId = product.getId();
                    product.setStocks(product.getStocks() + itemQuantityMap.getOrDefault(productId, 0));
                    productRepository.save(product);
                }
        );

        productReservationRepository.delete(productReservations);
    }

}
