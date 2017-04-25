package com.gavin.client.address;

import com.gavin.constants.ResponseCodeConstants;
import com.gavin.model.StandardResponseBody;
import com.gavin.model.vo.address.AddressVo;
import org.springframework.stereotype.Component;

@Component
public class AddressClientFallback implements AddressClient {

    @Override
    public StandardResponseBody<AddressVo> findAddressById(String _addressId) {
        StandardResponseBody<AddressVo> response = new StandardResponseBody<>();
        response.setCode(ResponseCodeConstants.REMOTE_CALL_FAILED);
        return response;
    }

}
