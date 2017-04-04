package com.gavin.client.product;

import com.gavin.constants.ResponseCodeConstants;
import com.gavin.model.Response;
import com.gavin.model.dto.order.ItemDto;
import com.gavin.model.dto.product.ProductReservationDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductClientFallback implements ProductClient {

    @Override
    public Response<List<ProductReservationDto>> reserveProducts(String _orderId, List<ItemDto> _items) {
        Response<List<ProductReservationDto>> response = new Response<>();
        response.setCode(ResponseCodeConstants.REMOTE_CALL_FAILED);
        return response;
    }

}
