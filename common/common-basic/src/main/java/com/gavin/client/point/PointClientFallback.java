package com.gavin.client.point;

import com.gavin.constants.ResponseCodeConstants;
import com.gavin.model.dto.point.FreezePointsDto;
import com.gavin.model.CustomResponseBody;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PointClientFallback implements PointClient {

    @Override
    public CustomResponseBody<BigDecimal> queryUsableAmount(String _accountId) {
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
