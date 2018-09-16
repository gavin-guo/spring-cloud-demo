package com.gavin.common.client.user;

import com.gavin.common.dto.user.AddressDto;
import com.gavin.common.dto.common.CustomResponseBody;
import com.gavin.common.dto.user.FreezePointsDto;
import com.gavin.common.dto.user.UserDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;

@FeignClient(value = "user", fallbackFactory = UserClientFallbackFactory.class)
public interface UserClient {

    @RequestMapping(value = "/users/loading", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    CustomResponseBody<UserDto> loadUserByLoginName(@RequestParam("login_name") String _loginName);

    @RequestMapping(value = "/users/points/calculation", method = RequestMethod.GET)
    CustomResponseBody<BigDecimal> calculateUsableAmount(@RequestParam(value = "user_id") String _userId);

    @RequestMapping(value = "/users/points/freeze", method = RequestMethod.PUT)
    CustomResponseBody freezePoints(@Valid @RequestBody FreezePointsDto _freeze);

    @RequestMapping(value = "/users/addresses/{address_id}", method = RequestMethod.GET)
    CustomResponseBody<AddressDto> findAddressById(@PathVariable("address_id") String _addressId);

    @RequestMapping(value = "/users/addresses/default", method = RequestMethod.GET)
    CustomResponseBody<AddressDto> findDefaultAddress();

}
