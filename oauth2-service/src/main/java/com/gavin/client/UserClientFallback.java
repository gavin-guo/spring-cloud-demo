package com.gavin.client;

import com.gavin.constants.ResponseCodeConstants;
import com.gavin.model.CustomResponseBody;
import com.gavin.model.dto.user.UserDto;
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
