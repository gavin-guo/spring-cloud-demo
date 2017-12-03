package com.gavin.business;

import com.gavin.common.messaging.ArrangeShipmentProcessor;
import com.gavin.common.messaging.CancelReservationProcessor;
import com.gavin.common.messaging.PaymentSucceededProcessor;
import com.gavin.common.messaging.WaitingForPaymentProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.stream.annotation.EnableBinding;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(scanBasePackages = {"com.gavin.business", "com.gavin.common"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = ("com.gavin.common.client"))
@EnableHystrix
@EnableCaching
@EnableBinding({
        ArrangeShipmentProcessor.class,
        CancelReservationProcessor.class,
        WaitingForPaymentProcessor.class,
        PaymentSucceededProcessor.class})
@EnableSwagger2
public class OrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }

}
