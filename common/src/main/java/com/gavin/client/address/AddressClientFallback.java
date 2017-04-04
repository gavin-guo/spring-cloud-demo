package com.gavin.client.address;

import com.gavin.constants.ResponseCodeConstants;
import com.gavin.model.Response;
import com.gavin.model.vo.address.AddressVo;
import org.springframework.stereotype.Component;

@Component
public class AddressClientFallback implements AddressClient {

    @Override
    public Response<AddressVo> findAddressById(String _addressId) {
        Response<AddressVo> response = new Response<>();
        response.setCode(ResponseCodeConstants.REMOTE_CALL_FAILED);
        return response;
    }

}
