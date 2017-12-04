package com.gavin.common.client.address;

import com.gavin.common.constants.ResponseCodeConstants;
import com.gavin.common.dto.address.AddressDto;
import com.gavin.common.dto.common.CustomResponseBody;
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

                CustomResponseBody<AddressDto> responseBody = new CustomResponseBody<>();
                responseBody.setCode(ResponseCodeConstants.REMOTE_CALL_FAILED);
                return responseBody;
            }

            @Override
            public CustomResponseBody<AddressDto> findDefaultAddress() {
                log.error(cause.getMessage());

                CustomResponseBody<AddressDto> responseBody = new CustomResponseBody<>();
                responseBody.setCode(ResponseCodeConstants.REMOTE_CALL_FAILED);
                return responseBody;
            }
        };
    }

}
