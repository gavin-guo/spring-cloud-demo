package com.gavin.controller;

import com.gavin.constants.ResponseCodeConstants;
import com.gavin.model.PageArgument;
import com.gavin.model.Response;
import com.gavin.model.dto.order.ItemDto;
import com.gavin.model.dto.product.CreateProductDto;
import com.gavin.model.dto.product.ProductReservationDto;
import com.gavin.model.vo.product.ProductVo;
import com.gavin.service.ProductService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@Api(value = "/products", description = "商品相关API")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @RequestMapping(value = "/products", method = RequestMethod.POST)
    @ApiOperation(value = "创建单件商品")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", dataType = "String", required = true, value = "Token", defaultValue = "bearer ")
    })
    public Response<ProductVo> createProduct(
            @ApiParam(name = "product", value = "要创建的商品信息", required = true) @Valid @RequestBody CreateProductDto _product) {
        Response<ProductVo> response = new Response<>(ResponseCodeConstants.SUCCESS);

        ProductVo productVo = productService.createProduct(_product);
        log.info("商品{}录入成功。", productVo.getName());

        response.setContents(productVo);
        return response;
    }

    @RequestMapping(value = "/products/{product_id}", method = RequestMethod.GET)
    @ApiOperation(value = "根据商品ID查询单件商品信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", dataType = "String", required = true, value = "Token", defaultValue = "bearer ")
    })
    public Response<ProductVo> findProductById(
            @ApiParam(name = "product_id", value = "要查询的商品ID", required = true) @PathVariable("product_id") String _productId) {
        Response<ProductVo> response = new Response<>(ResponseCodeConstants.SUCCESS);

        ProductVo productVo = productService.findProductById(_productId);
        response.setContents(productVo);
        return response;
    }

    @RequestMapping(value = "/products/category/{category_id}", method = RequestMethod.GET)
    @ApiOperation(value = "查询属于指定类型的所有商品信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", dataType = "String", required = true, value = "Token", defaultValue = "bearer ")
    })
    public Response<List<ProductVo>> findProductByCategoryId(
            @ApiParam(name = "category_id", value = "类型ID", required = true) @PathVariable("category_id") String _categoryId,
            @ApiParam(name = "current_page", value = "当前页", required = true) @RequestParam("current_page") Integer _currentPage,
            @ApiParam(name = "page_size", value = "每页显示记录数", required = true) @RequestParam("page_size") Integer _pageSize) {
        Response<List<ProductVo>> response = new Response<>(ResponseCodeConstants.SUCCESS);

        PageArgument pageArgument = new PageArgument();
        pageArgument.setCurrentPage(_currentPage);
        pageArgument.setPageSize(_pageSize);

        List<ProductVo> productVos = productService.findProductByCategoryId(_categoryId, pageArgument);
        response.setContents(productVos);
        response.setPage(pageArgument);
        return response;
    }

    @RequestMapping(value = "/products/reservation/{order_id}", method = RequestMethod.PUT)
    @ApiOperation(value = "为订单保留指定类型和数量的商品")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", dataType = "String", required = true, value = "Token", defaultValue = "bearer ")
    })
    public Response<List<ProductReservationDto>> reserveProducts(
            @ApiParam(name = "order_id", value = "订单ID", required = true) @PathVariable("order_id") String _orderId,
            @ApiParam(name = "items", value = "要预约的商品", required = true) @Valid @RequestBody List<ItemDto> _items) {
        Response<List<ProductReservationDto>> response = new Response<>(ResponseCodeConstants.SUCCESS);

        List<ProductReservationDto> productDetailVOs = productService.reserveProducts(_orderId, _items);
        response.setContents(productDetailVOs);
        return response;
    }

}
