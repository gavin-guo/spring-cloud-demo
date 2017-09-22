package com.gavin.client.address;

import com.gavin.constants.ResponseCodeConstants;
import com.gavin.dto.common.CustomResponseBody;
import com.gavin.dto.address.AddressDto;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AddressClientFallbackFactory implements FallbackFactory<AddressClient> {

    @Override
    public AddressClient create(Throwable cause) {
        return new AddressClient() {
            @Override
            public CustomResponseBody<AddressDto> findAddressById(String _addressId) {
                log.error(cause.getMessage());

                CustomResponseBody<AddressDto> response = new CustomResponseBody<>();
                response.setResultCode(ResponseCodeConstants.REMOTE_CALL_FAILED);
                return response;
            }

            @Override
            public CustomResponseBody<AddressDto> findDefaultAddress() {
                log.error(cause.getMessage());

                CustomResponseBody<AddressDto> response = new CustomResponseBody<>();
                response.setResultCode(ResponseCodeConstants.REMOTE_CALL_FAILED);
                return response;
            }
        };
    }

}
