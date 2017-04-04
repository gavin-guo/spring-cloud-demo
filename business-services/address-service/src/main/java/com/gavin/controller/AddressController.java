package com.gavin.controller;

import com.gavin.constants.ResponseCodeConstants;
import com.gavin.model.Response;
import com.gavin.model.dto.address.RegisterAddressDto;
import com.gavin.model.vo.address.AddressVo;
import com.gavin.service.AddressService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Slf4j
@Api(value = "/addresses", description = "地址相关API")
public class AddressController {

    private final AddressService addressService;

    @Autowired
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @RequestMapping(value = "/addresses", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "登记单个地址")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", dataType = "String", required = true, value = "Token", defaultValue = "bearer ")
    })
    public Response<AddressVo> createAddress(
            @ApiParam(name = "address", value = "要登记的地址信息", required = true) @Valid @RequestBody RegisterAddressDto _address) {
        Response<AddressVo> response = new Response<>(ResponseCodeConstants.SUCCESS);

        AddressVo addressVo = addressService.registerAddress(_address);
        log.info("地址{}登录成功。", addressVo.getAddress());

        response.setContents(addressVo);
        return response;
    }

    @RequestMapping(value = "/addresses/{address_id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "根据地址ID查询单个地址")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", dataType = "String", required = true, value = "Token", defaultValue = "bearer ")
    })
    public Response<AddressVo> findAddressById(
            @ApiParam(name = "address_id", value = "要查询的地址ID", required = true) @PathVariable("address_id") String _addressId) {
        Response<AddressVo> response = new Response<>(ResponseCodeConstants.SUCCESS);

        AddressVo addressVo = addressService.findAddressById(_addressId);
        response.setContents(addressVo);
        return response;
    }

}
