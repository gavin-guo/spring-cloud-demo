package com.gavin.client.point;

import com.gavin.config.OAuth2FeignConfiguration;
import com.gavin.model.Response;
import com.gavin.model.dto.point.FreezePointsDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.math.BigDecimal;

@FeignClient(value = "point-service", fallback = PointClientFallback.class, configuration = {OAuth2FeignConfiguration.class})
public interface PointClient {

    @RequestMapping(value = "/points/usable", method = RequestMethod.GET)
    Response<BigDecimal> queryUsableAmount(@RequestParam(value = "account_id") String _accountId);

    @RequestMapping(value = "/points/freeze", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    Response freezePoints(@Valid @RequestBody FreezePointsDto _freeze);

}