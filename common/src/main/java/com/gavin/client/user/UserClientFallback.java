package com.gavin.client.user;

import com.gavin.constants.ResponseCodeConstants;
import com.gavin.dto.common.CustomResponseBody;
import com.gavin.dto.user.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserClientFallback implements UserClient {

    @Override
    public CustomResponseBody<UserDto> loadUserByLoginName(String _loginName) {
        CustomResponseBody<UserDto> response = new CustomResponseBody<>();
        response.setResultCode(ResponseCodeConstants.REMOTE_CALL_FAILED);
        return response;
    }

}
