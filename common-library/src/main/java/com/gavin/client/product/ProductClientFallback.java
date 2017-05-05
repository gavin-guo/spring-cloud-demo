package com.gavin.client.product;

import com.gavin.constants.ResponseCodeConstants;
import com.gavin.model.CustomResponseBody;
import com.gavin.model.dto.order.ItemDto;
import com.gavin.model.dto.product.ReservedProductDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductClientFallback implements ProductClient {

    @Override
    public CustomResponseBody<List<ReservedProductDto>> reserveProducts(String _orderId, List<ItemDto> _items) {
        CustomResponseBody<List<ReservedProductDto>> response = new CustomResponseBody<>();
        response.setResultCode(ResponseCodeConstants.REMOTE_CALL_FAILED);
        return response;
    }

}
