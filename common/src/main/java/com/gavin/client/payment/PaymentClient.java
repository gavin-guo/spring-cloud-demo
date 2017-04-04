package com.gavin.client.payment;

import com.gavin.config.OAuth2FeignConfiguration;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(value = "payment-service", fallback = PaymentClientFallback.class, configuration = {OAuth2FeignConfiguration.class})
public interface PaymentClient {

}
