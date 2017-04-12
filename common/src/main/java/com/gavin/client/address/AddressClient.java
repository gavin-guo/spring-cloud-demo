package com.gavin.client.address;

import com.gavin.model.Response;
import com.gavin.model.vo.address.AddressVo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "address-service", fallback = AddressClientFallback.class)
public interface AddressClient {

    @RequestMapping(value = "/addresses/{address_id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    Response<AddressVo> findAddressById(@PathVariable("address_id") String _addressId);

}
