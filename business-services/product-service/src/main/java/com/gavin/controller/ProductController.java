package com.gavin.controller;

import com.gavin.dto.common.PageResult;
import com.gavin.dto.order.ItemDto;
import com.gavin.dto.product.CreateProductDto;
import com.gavin.dto.product.ProductDto;
import com.gavin.dto.product.ReservedProductDto;
import com.gavin.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@Api(value = "/products", description = "商品相关API")
public class ProductController {

    @Autowired
    private ProductService productService;

    @RequestMapping(value = "/products", method = RequestMethod.POST)
    @ApiOperation(value = "创建单件商品")
    public ProductDto createProduct(
            @ApiParam(name = "product", value = "要创建的商品信息", required = true) @Valid @RequestBody CreateProductDto _product) {
        return productService.createProduct(_product);
    }

    @RequestMapping(value = "/products/{product_id}", method = RequestMethod.GET)
    @ApiOperation(value = "根据商品ID查询单件商品信息")
    public ProductDto findProductById(
            @ApiParam(name = "product_id", value = "要查询的商品ID", required = true) @PathVariable("product_id") String _productId) {
        return productService.findProductById(_productId);
    }

    @RequestMapping(value = "/products/category/{category_id}", method = RequestMethod.GET)
    @ApiOperation(value = "查询属于指定类型的所有商品信息")
    public PageResult<ProductDto> findProductByCategoryId(
            @ApiParam(name = "category_id", value = "类型ID", required = true) @PathVariable("category_id") String _categoryId,
            @ApiParam(name = "current_page", value = "当前页") @RequestParam(name = "current_page", defaultValue = "1") Integer _currentPage,
            @ApiParam(name = "page_size", value = "每页显示记录数") @RequestParam(name = "page_size", defaultValue = "10") Integer _pageSize) {
        PageRequest pageRequest = new PageRequest(
                _currentPage - 1,
                _pageSize,
                new Sort(Sort.Direction.ASC, "id")
        );

        PageResult<ProductDto> pageResult = productService.findProductByCategoryId(_categoryId, pageRequest);
        pageResult.setCurrentPage(_currentPage);
        return pageResult;
    }

    @RequestMapping(value = "/products/reservation", method = RequestMethod.PUT)
    @ApiOperation(value = "为订单保留指定类型和数量的商品")
    public List<ReservedProductDto> reserveProducts(
            @ApiParam(name = "order_id", value = "订单ID", required = true) @RequestParam("order_id") String _orderId,
            @ApiParam(name = "items", value = "要预约的商品", required = true) @Valid @RequestBody List<ItemDto> _items) {
        return productService.reserveProducts(_orderId, _items);
    }

}
