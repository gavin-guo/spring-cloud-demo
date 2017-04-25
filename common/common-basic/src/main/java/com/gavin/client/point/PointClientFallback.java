package com.gavin.client.point;

import com.gavin.constants.ResponseCodeConstants;
import com.gavin.model.StandardResponseBody;
import com.gavin.model.dto.point.FreezePointsDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PointClientFallback implements PointClient {

    @Override
    public StandardResponseBody<BigDecimal> queryUsableAmount(String _accountId) {
        StandardResponseBody<BigDecimal> response = new StandardResponseBody<>();
        response.setCode(ResponseCodeConstants.REMOTE_CALL_FAILED);
        return response;
    }

    @Override
    public StandardResponseBody freezePoints(FreezePointsDto _freeze) {
        StandardResponseBody response = new StandardResponseBody<>();
        response.setCode(ResponseCodeConstants.REMOTE_CALL_FAILED);
        return response;
    }

}
