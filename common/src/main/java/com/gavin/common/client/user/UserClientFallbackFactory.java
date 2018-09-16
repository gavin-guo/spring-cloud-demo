package com.gavin.common.client.user;

import com.gavin.common.constants.ResponseCodeConstants;
import com.gavin.common.dto.user.AddressDto;
import com.gavin.common.dto.common.CustomResponseBody;
import com.gavin.common.dto.user.FreezePointsDto;
import com.gavin.common.dto.user.UserDto;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Slf4j
public class UserClientFallbackFactory implements FallbackFactory<UserClient> {

    @Override
    public UserClient create(Throwable cause) {
        return new UserClient() {
            @Override
            public CustomResponseBody<UserDto> loadUserByLoginName(String _loginName) {
                log.error(cause.getMessage());

                CustomResponseBody<UserDto> responseBody = new CustomResponseBody<>();
                responseBody.setCode(ResponseCodeConstants.REMOTE_CALL_FAILED);
                return responseBody;
            }

            @Override
            public CustomResponseBody<BigDecimal> calculateUsableAmount(String _userId) {
                log.error(cause.getMessage());

                CustomResponseBody<BigDecimal> responseBody = new CustomResponseBody<>();
                responseBody.setCode(ResponseCodeConstants.REMOTE_CALL_FAILED);
                return responseBody;
            }

            @Override
            public CustomResponseBody freezePoints(FreezePointsDto _freeze) {
                log.error(cause.getMessage());

                CustomResponseBody responseBody = new CustomResponseBody<>();
                responseBody.setCode(ResponseCodeConstants.REMOTE_CALL_FAILED);
                return responseBody;
            }

            @Override
            public CustomResponseBody<AddressDto> findAddressById(String _addressId) {
                log.error(cause.getMessage());

                CustomResponseBody<AddressDto> responseBody = new CustomResponseBody<>();
                responseBody.setCode(ResponseCodeConstants.REMOTE_CALL_FAILED);
                return responseBody;
            }

            @Override
            public CustomResponseBody<AddressDto> findDefaultAddress() {
                log.error(cause.getMessage());

                CustomResponseBody<AddressDto> responseBody = new CustomResponseBody<>();
                responseBody.setCode(ResponseCodeConstants.REMOTE_CALL_FAILED);
                return responseBody;
            }
        };
    }

}
