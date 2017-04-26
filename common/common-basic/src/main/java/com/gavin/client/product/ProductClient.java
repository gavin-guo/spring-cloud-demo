package com.gavin.client.product;

import com.gavin.model.response.ExecutionResponseBody;
import com.gavin.model.dto.order.ItemDto;
import com.gavin.model.dto.product.ProductReservationDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(value = "product-service", fallback = ProductClientFallback.class)
public interface ProductClient {

    @RequestMapping(value = "/products/reservation/{order_id}", method = RequestMethod.PUT)
    ExecutionResponseBody<List<ProductReservationDto>> reserveProducts(
            @PathVariable("order_id") String _orderId,
            @RequestBody List<ItemDto> _items);

}