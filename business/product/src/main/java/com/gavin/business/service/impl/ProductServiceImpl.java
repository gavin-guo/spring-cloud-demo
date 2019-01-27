package com.gavin.business.service.impl;

import com.gavin.business.domain.Category;
import com.gavin.business.domain.Product;
import com.gavin.business.domain.ProductReservation;
import com.gavin.business.domain.ProductStock;
import com.gavin.business.exception.InsufficientInventoryException;
import com.gavin.business.repository.*;
import com.gavin.business.service.ProductService;
import com.gavin.common.dto.common.PageResult;
import com.gavin.common.dto.order.ItemDto;
import com.gavin.common.dto.product.CreateProductDto;
import com.gavin.common.dto.product.ProductDto;
import com.gavin.common.dto.product.ReservedProductDto;
import com.gavin.common.exception.RecordNotFoundException;
import com.gavin.common.util.JsonUtils;
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
    private ProductStockRepository productStockRepository;

    @Autowired
    private ProductReservationRepository productReservationRepository;

    @Autowired
    private PointRewardPlanRepository pointRewardPlanRepository;

    @Override
    @Transactional
    public ProductDto createProduct(CreateProductDto _product) {
        String categoryId = _product.getCategoryId();
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RecordNotFoundException("category", categoryId));

        Product product = modelMapper.map(_product, Product.class);
        productRepository.save(product);

        ProductStock productStock = new ProductStock();
        productStock.setProductId(product.getId());
        productStock.setStocks(_product.getStocks());
        productStockRepository.save(productStock);

        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        productDto.setCategoryName(category.getName());

        log.info("create product successfully. {}", JsonUtils.toJson(productDto));

        return productDto;
    }

    @Override
    public ProductDto findProductById(String _productId) {
        Product product = productRepository.findById(_productId)
                .orElseThrow(() -> new RecordNotFoundException("product", _productId));

        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        Category category = categoryRepository.findById(product.getCategoryId()).orElse(null);
        productDto.setCategoryName(category.getName());

        return productDto;
    }

    @Override
    public PageResult<ProductDto> findProductByCategoryId(String _categoryId, PageRequest _pageRequest) {
        Category category = categoryRepository.findById(_categoryId)
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

        List<Product> products = productRepository.findAllById(productIds);
        Map<String, Product> productMap = products.stream().collect(
                Collectors.toMap(Product::getId, product -> product));

        List<ProductStock> productStocks = productStockRepository.findAllByProductIdIn(productIds);
        Map<String, ProductStock> productStockMap = productStocks.stream().collect(
                Collectors.toMap(ProductStock::getProductId, productStock -> productStock));

        List<ReservedProductDto> reservedProductDtos = new ArrayList<>();

        _items.forEach(
                item -> {
                    String productId = item.getProductId();
                    Product product = productMap.get(productId);
                    ProductStock productStock = productStockMap.get(productId);

                    // 订单中此商品的订购数超过库存。
                    if (item.getQuantity() > productStock.getStocks()) {
                        log.warn("{} of product({}) on order, but {} in stock.", item.getQuantity(), product.getName(), productStock.getStocks());
                        throw new InsufficientInventoryException(String.format("product %s is insufficient.", product.getName()));
                    }

                    // 从该商品的库存中冻结与订单相应的数目。
                    productStock.setStocks(productStock.getStocks() - item.getQuantity());
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

        log.info("reserve products successfully. {}", JsonUtils.toJson(reservedProductDtos));

        return reservedProductDtos;
    }

    @Override
    @Transactional
    public void cancelReservation(String _orderId) {
        List<ProductReservation> productReservations = productReservationRepository.findByOrderId(_orderId);

        List<String> productIds = productReservations.stream()
                .map(ProductReservation::getProductId)
                .collect(Collectors.toList());

        List<ProductStock> productStocks = productStockRepository.findAllByProductIdIn(productIds);

        Map<String, Integer> itemQuantityMap = productReservations.stream()
                .collect(Collectors.toMap(
                        ProductReservation::getProductId,
                        ProductReservation::getQuantity));

        productStocks.forEach(
                productStock -> {
                    String productId = productStock.getId();
                    productStock.setStocks(productStock.getStocks() + itemQuantityMap.getOrDefault(productId, 0));
                    productStockRepository.save(productStock);
                }
        );

        productReservationRepository.deleteAll(productReservations);
    }

}
