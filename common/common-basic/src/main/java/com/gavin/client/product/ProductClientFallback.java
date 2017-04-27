package com.gavin.client.product;

import com.gavin.model.dto.order.ItemDto;
import com.gavin.model.dto.product.ProductReservationDto;
import com.gavin.model.response.StandardResponseBody;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductClientFallback implements ProductClient {

    @Override
    public StandardResponseBody<List<ProductReservationDto>> reserveProducts(String _orderId, List<ItemDto> _items) {
        return null;
    }

}
