package com.gavin.common.client.point;

import com.gavin.common.dto.common.CustomResponse;
import com.gavin.common.dto.point.FreezePointsDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.math.BigDecimal;

@FeignClient(value = "point", fallbackFactory = PointClientFallbackFactory.class)
public interface PointClient {

    @RequestMapping(value = "/points/calculation", method = RequestMethod.GET)
    CustomResponse<BigDecimal> calculateUsableAmount(@RequestParam(value = "user_id") String _userId);

    @RequestMapping(value = "/points/freeze", method = RequestMethod.PUT)
    CustomResponse freezePoints(@Valid @RequestBody FreezePointsDto _freeze);

}