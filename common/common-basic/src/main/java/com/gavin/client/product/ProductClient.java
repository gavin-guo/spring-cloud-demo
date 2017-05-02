package com.gavin.client.product;

import com.gavin.model.StandardResponseBody;
import com.gavin.model.dto.order.ItemDto;
import com.gavin.model.dto.product.ReserveProductsDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "product-service", fallback = ProductClientFallback.class)
public interface ProductClient {

    @RequestMapping(value = "/products/reservation", method = RequestMethod.PUT)
    StandardResponseBody<List<ReserveProductsDto>> reserveProducts(
            @RequestParam("order_id") String _orderId,
            @RequestBody List<ItemDto> _items);

}