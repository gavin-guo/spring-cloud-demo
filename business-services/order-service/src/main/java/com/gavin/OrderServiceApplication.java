package com.gavin;

import com.gavin.messaging.ArrangeShipmentProcessor;
import com.gavin.messaging.CancelReservationProcessor;
import com.gavin.messaging.PaymentSucceededProcessor;
import com.gavin.messaging.WaitingForPaymentProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableCircuitBreaker
@EnableCaching
@EnableBinding({
        ArrangeShipmentProcessor.class,
        CancelReservationProcessor.class,
        WaitingForPaymentProcessor.class,
        PaymentSucceededProcessor.class})
@EnableSwagger2
public class OrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }

}
