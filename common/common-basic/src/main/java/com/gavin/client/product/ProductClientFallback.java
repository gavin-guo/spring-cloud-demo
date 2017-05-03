package com.gavin.client.product;

import com.gavin.model.dto.order.ItemDto;
import com.gavin.model.dto.product.ReserveProductsDto;
import com.gavin.model.CustomResponseBody;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductClientFallback implements ProductClient {

    @Override
    public CustomResponseBody<List<ReserveProductsDto>> reserveProducts(String _orderId, List<ItemDto> _items) {
        return null;
    }

}
