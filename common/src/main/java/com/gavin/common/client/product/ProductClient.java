package com.gavin.common.client.product;

import com.gavin.common.dto.common.CustomResponseBody;
import com.gavin.common.dto.order.ItemDto;
import com.gavin.common.dto.product.ReservedProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "product", fallbackFactory = ProductClientFallbackFactory.class)
public interface ProductClient {

    @RequestMapping(value = "/products/reservation", method = RequestMethod.PUT)
    CustomResponseBody<ReservedProductDto> reserveProducts(
            @RequestParam("order_id") String _orderId,
            @RequestBody List<ItemDto> _items);

}