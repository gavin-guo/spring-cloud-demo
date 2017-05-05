package com.gavin.client.payment;

import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(value = "payment-service", fallback = PaymentClientFallback.class)
public interface PaymentClient {

}
