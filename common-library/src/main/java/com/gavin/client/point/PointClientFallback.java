package com.gavin.client.point;

import com.gavin.constants.ResponseCodeConstants;
import com.gavin.model.CustomResponseBody;
import com.gavin.model.dto.point.FreezePointsDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PointClientFallback implements PointClient {

    @Override
    public CustomResponseBody<BigDecimal> calculateUsableAmount(String _userId) {
        CustomResponseBody<BigDecimal> response = new CustomResponseBody<>();
        response.setResultCode(ResponseCodeConstants.REMOTE_CALL_FAILED);
        return response;
    }

    @Override
    public CustomResponseBody freezePoints(FreezePointsDto _freeze) {
        CustomResponseBody response = new CustomResponseBody<>();
        response.setResultCode(ResponseCodeConstants.REMOTE_CALL_FAILED);
        return response;
    }

}
