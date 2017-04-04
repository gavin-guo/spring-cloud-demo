package com.gavin.service;

import com.gavin.model.PageArgument;
import com.gavin.model.dto.order.ItemDto;
import com.gavin.model.dto.product.CreateProductDto;
import com.gavin.model.dto.product.ProductReservationDto;
import com.gavin.model.vo.product.ProductVo;

import java.util.List;

public interface ProductService {

    /**
     * 创建商品。
     *
     * @param _product 创建商品信息
     * @return 商品信息
     */
    ProductVo createProduct(CreateProductDto _product);

    /**
     * 通过ID查询商品。
     *
     * @param _productId 商品ID
     * @return 商品信息
     */
    ProductVo findProductById(String _productId);

    /**
     * 通过类别查询商品。
     *
     * @param _categoryId   类别ID
     * @param _pageArgument 分页设置
     * @return
     */
    List<ProductVo> findProductByCategoryId(String _categoryId, PageArgument _pageArgument);

    /**
     * 为订单保留指定数量的商品。
     *
     * @param _orderId
     * @param _items
     * @return
     */
    List<ProductReservationDto> reserveProducts(String _orderId, List<ItemDto> _items);

    /**
     * 取消商品的预约。
     *
     * @param _orderId 订单ID
     */
    void cancelReservation(String _orderId);

}
