package com.gavin.client.point;

import com.gavin.constants.ResponseCodeConstants;
import com.gavin.model.Response;
import com.gavin.model.dto.point.FreezePointsDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PointClientFallback implements PointClient {

    @Override
    public Response<BigDecimal> queryUsableAmount(String _accountId) {
        Response<BigDecimal> response = new Response<>();
        response.setCode(ResponseCodeConstants.REMOTE_CALL_FAILED);
        return response;
    }

    @Override
    public Response freezePoints(FreezePointsDto _freeze) {
        Response response = new Response<>();
        response.setCode(ResponseCodeConstants.REMOTE_CALL_FAILED);
        return response;
    }

}
