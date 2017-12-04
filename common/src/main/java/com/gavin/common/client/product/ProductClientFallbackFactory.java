package com.gavin.common.client.product;

import com.gavin.common.constants.ResponseCodeConstants;
import com.gavin.common.dto.common.CustomResponseBody;
import com.gavin.common.dto.order.ItemDto;
import com.gavin.common.dto.product.ReservedProductDto;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class ProductClientFallbackFactory implements FallbackFactory<ProductClient> {

    @Override
    public ProductClient create(Throwable cause) {
        return new ProductClient() {
            @Override
            public CustomResponseBody<ReservedProductDto> reserveProducts(String _orderId, List<ItemDto> _items) {
                log.error(cause.getMessage());

                CustomResponseBody<ReservedProductDto> responseBody = new CustomResponseBody<>();
                responseBody.setCode(ResponseCodeConstants.REMOTE_CALL_FAILED);
                return responseBody;
            }
        };
    }

}
