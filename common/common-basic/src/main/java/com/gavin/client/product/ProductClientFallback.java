package com.gavin.client.product;

import com.gavin.constants.ResponseCodeConstants;
import com.gavin.model.response.ExecutionResponseBody;
import com.gavin.model.dto.order.ItemDto;
import com.gavin.model.dto.product.ProductReservationDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductClientFallback implements ProductClient {

    @Override
    public ExecutionResponseBody<List<ProductReservationDto>> reserveProducts(String _orderId, List<ItemDto> _items) {
        ExecutionResponseBody<List<ProductReservationDto>> response = new ExecutionResponseBody<>();
        response.setCode(ResponseCodeConstants.REMOTE_CALL_FAILED);
        return response;
    }

}
