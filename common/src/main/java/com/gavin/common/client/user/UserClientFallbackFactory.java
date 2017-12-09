package com.gavin.common.client.user;

import com.gavin.common.constants.ResponseCodeConstants;
import com.gavin.common.dto.common.CustomResponseBody;
import com.gavin.common.dto.user.UserDto;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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
        };
    }

}
