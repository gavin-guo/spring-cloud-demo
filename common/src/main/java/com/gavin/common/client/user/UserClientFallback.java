package com.gavin.common.client.user;

import com.gavin.common.constants.ResponseCodeConstants;
import com.gavin.common.dto.common.CustomResponseBody;
import com.gavin.common.dto.user.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserClientFallback implements UserClient {

    @Override
    public CustomResponseBody<UserDto> loadUserByLoginName(String _loginName) {
        CustomResponseBody<UserDto> responseBody = new CustomResponseBody<>();
        responseBody.setCode(ResponseCodeConstants.REMOTE_CALL_FAILED);
        return responseBody;
    }

}
