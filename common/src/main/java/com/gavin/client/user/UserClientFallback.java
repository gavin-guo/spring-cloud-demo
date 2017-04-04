package com.gavin.client.user;

import com.gavin.constants.ResponseCodeConstants;
import com.gavin.model.Response;
import com.gavin.model.dto.user.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserClientFallback implements UserClient {

    @Override
    public Response<UserDto> loadUserByLoginName(String _loginName) {
        Response<UserDto> response = new Response<>();
        response.setCode(ResponseCodeConstants.REMOTE_CALL_FAILED);
        return response;
    }

}
