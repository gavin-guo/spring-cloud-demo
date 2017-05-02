package com.gavin.client.address;

import com.gavin.constants.ResponseCodeConstants;
import com.gavin.model.StandardResponseBody;
import com.gavin.model.dto.address.AddressDto;
import org.springframework.stereotype.Component;

@Component
public class AddressClientFallback implements AddressClient {

    @Override
    public StandardResponseBody<AddressDto> findAddressById(String _addressId) {
        StandardResponseBody<AddressDto> response = new StandardResponseBody<>();
        response.setResultCode(ResponseCodeConstants.REMOTE_CALL_FAILED);
        return response;
    }

}
