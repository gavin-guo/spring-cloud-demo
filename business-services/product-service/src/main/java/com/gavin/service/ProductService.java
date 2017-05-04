package com.gavin.service;

import com.gavin.model.PageResult;
import com.gavin.model.dto.order.ItemDto;
import com.gavin.model.dto.product.CreateProductDto;
import com.gavin.model.dto.product.ProductDto;
import com.gavin.model.dto.product.ReservedProductDto;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface ProductService {

    /**
     * 创建商品。
     *
     * @param _product 创建商品信息
     * @return 商品信息
     */
    ProductDto createProduct(CreateProductDto _product);

    /**
     * 通过ID查询商品。
     *
     * @param _productId 商品ID
     * @return 商品信息
     */
    ProductDto findProductById(String _productId);

    /**
     * 通过类别查询商品。
     *
     * @param _categoryId  类别ID
     * @param _pageRequest 分页设置
     * @return
     */
    PageResult<ProductDto> findProductByCategoryId(String _categoryId, PageRequest _pageRequest);

    /**
     * 为订单保留指定数量的商品。
     *
     * @param _orderId
     * @param _items
     * @return
     */
    List<ReservedProductDto> reserveProducts(String _orderId, List<ItemDto> _items);

    /**
     * 取消商品的预约。
     *
     * @param _orderId 订单ID
     */
    void cancelReservation(String _orderId);

}
