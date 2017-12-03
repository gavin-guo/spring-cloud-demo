package com.gavin.common.client.user;

import com.gavin.common.constants.ResponseCodeConstants;
import com.gavin.common.dto.common.CustomResponse;
import com.gavin.common.dto.user.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserClientFallback implements UserClient {

    @Override
    public CustomResponse<UserDto> loadUserByLoginName(String _loginName) {
        CustomResponse<UserDto> response = new CustomResponse<>();
        response.setCode(ResponseCodeConstants.REMOTE_CALL_FAILED);
        return response;
    }

}
