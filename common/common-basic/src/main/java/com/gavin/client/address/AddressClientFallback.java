package com.gavin.client.address;

import com.gavin.constants.ResponseCodeConstants;
import com.gavin.model.response.ExecutionResponseBody;
import com.gavin.model.vo.address.AddressVo;
import org.springframework.stereotype.Component;

@Component
public class AddressClientFallback implements AddressClient {

    @Override
    public ExecutionResponseBody<AddressVo> findAddressById(String _addressId) {
        ExecutionResponseBody<AddressVo> response = new ExecutionResponseBody<>();
        response.setCode(ResponseCodeConstants.REMOTE_CALL_FAILED);
        return response;
    }

}
