package com.gavin.business.controller;

import com.gavin.business.service.AddressService;
import com.gavin.common.constants.RequestHeaderConstants;
import com.gavin.common.dto.user.AddressDto;
import com.gavin.common.dto.user.RegisterAddressDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@Api(value = "/addresses", description = "地址相关API")
@RequestMapping("/users")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @RequestMapping(value = "/addresses", method = RequestMethod.POST)
    @ApiOperation(value = "登记单个地址")
    public AddressDto registerAddress(
            @ApiParam(name = "x_user_id", value = "用户ID", required = true) @RequestHeader(RequestHeaderConstants.X_USER_ID) String _userId,
            @ApiParam(name = "address", value = "要登记的地址信息", required = true) @Valid @RequestBody RegisterAddressDto _address) {
        return addressService.registerAddress(_userId, _address);
    }

    @RequestMapping(value = "/addresses/{address_id}", method = RequestMethod.GET)
    @ApiOperation(value = "根据地址ID查询单个地址")
    public AddressDto findAddressById(
            @ApiParam(name = "address_id", value = "要查询的地址ID", required = true) @PathVariable("address_id") String _addressId) {
        return addressService.findAddressById(_addressId);
    }

    @RequestMapping(value = "/addresses", method = RequestMethod.GET)
    @ApiOperation(value = "查询用户的所有地址")
    public List<AddressDto> findAddresses(
            @ApiParam(name = "x_user_id", value = "用户ID", required = true) @RequestHeader(RequestHeaderConstants.X_USER_ID) String _userId) {
        return addressService.findAddressesByUserId(_userId);
    }

    @RequestMapping(value = "/addresses/default", method = RequestMethod.GET)
    @ApiOperation(value = "查询用户的默认地址")
    public AddressDto findDefaultAddress(
            @ApiParam(name = "x_user_id", value = "用户ID", required = true) @RequestHeader(RequestHeaderConstants.X_USER_ID) String _userId) {
        return addressService.findDefaultAddressByUserId(_userId);
    }

}
