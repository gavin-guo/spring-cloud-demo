package com.gavin.common.client.address;

import com.gavin.common.dto.common.CustomResponseBody;
import com.gavin.common.dto.address.AddressDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "address", fallbackFactory = AddressClientFallbackFactory.class)
public interface AddressClient {

    @RequestMapping(value = "/addresses/{address_id}", method = RequestMethod.GET)
    CustomResponseBody<AddressDto> findAddressById(@PathVariable("address_id") String _addressId);

    @RequestMapping(value = "/addresses/default", method = RequestMethod.GET)
    CustomResponseBody<AddressDto> findDefaultAddress();

}
