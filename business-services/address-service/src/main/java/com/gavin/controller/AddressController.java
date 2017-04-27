package com.gavin.controller;

import com.gavin.model.dto.address.AddressDto;
import com.gavin.model.dto.address.RegisterAddressDto;
import com.gavin.service.AddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Slf4j
@Api(value = "/addresses", description = "地址相关API")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @RequestMapping(value = "/addresses", method = RequestMethod.POST)
    @ApiOperation(value = "登记单个地址")
    public AddressDto registerAddress(
            @ApiParam(name = "address", value = "要登记的地址信息", required = true) @Valid @RequestBody RegisterAddressDto _address) {
        return addressService.registerAddress(_address);
    }

    @RequestMapping(value = "/addresses/{address_id}", method = RequestMethod.GET)
    @ApiOperation(value = "根据地址ID查询单个地址")
    public AddressDto findAddressById(
            @ApiParam(name = "address_id", value = "要查询的地址ID", required = true) @PathVariable("address_id") String _addressId) {
        return addressService.findAddressById(_addressId);
    }

}
