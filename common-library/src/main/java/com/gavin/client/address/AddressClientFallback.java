package com.gavin.client.address;

import com.gavin.constants.ResponseCodeConstants;
import com.gavin.model.CustomResponseBody;
import com.gavin.model.dto.address.AddressDto;
import org.springframework.stereotype.Component;

@Component
public class AddressClientFallback implements AddressClient {

    @Override
    public CustomResponseBody<AddressDto> findAddressById(String _addressId) {
        CustomResponseBody<AddressDto> response = new CustomResponseBody<>();
        response.setResultCode(ResponseCodeConstants.REMOTE_CALL_FAILED);
        return response;
    }

}
