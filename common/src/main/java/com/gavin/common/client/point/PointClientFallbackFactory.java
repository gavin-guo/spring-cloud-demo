package com.gavin.common.client.point;

import com.gavin.common.constants.ResponseCodeConstants;
import com.gavin.common.dto.common.CustomResponseBody;
import com.gavin.common.dto.point.FreezePointsDto;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Slf4j
public class PointClientFallbackFactory implements FallbackFactory<PointClient> {

    @Override
    public PointClient create(Throwable cause) {
        return new PointClient() {
            @Override
            public CustomResponseBody<BigDecimal> calculateUsableAmount(String _userId) {
                log.error(cause.getMessage());

                CustomResponseBody<BigDecimal> responseBody = new CustomResponseBody<>();
                responseBody.setCode(ResponseCodeConstants.REMOTE_CALL_FAILED);
                return responseBody;
            }

            @Override
            public CustomResponseBody freezePoints(FreezePointsDto _freeze) {
                log.error(cause.getMessage());

                CustomResponseBody responseBody = new CustomResponseBody<>();
                responseBody.setCode(ResponseCodeConstants.REMOTE_CALL_FAILED);
                return responseBody;
            }
        };
    }

}
