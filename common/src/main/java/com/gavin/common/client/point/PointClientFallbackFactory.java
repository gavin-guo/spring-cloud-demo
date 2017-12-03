package com.gavin.common.client.point;

import com.gavin.common.constants.ResponseCodeConstants;
import com.gavin.common.dto.common.CustomResponse;
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
            public CustomResponse<BigDecimal> calculateUsableAmount(String _userId) {
                log.error(cause.getMessage());

                CustomResponse<BigDecimal> response = new CustomResponse<>();
                response.setCode(ResponseCodeConstants.REMOTE_CALL_FAILED);
                return response;
            }

            @Override
            public CustomResponse freezePoints(FreezePointsDto _freeze) {
                log.error(cause.getMessage());

                CustomResponse response = new CustomResponse<>();
                response.setCode(ResponseCodeConstants.REMOTE_CALL_FAILED);
                return response;
            }
        };
    }

}
