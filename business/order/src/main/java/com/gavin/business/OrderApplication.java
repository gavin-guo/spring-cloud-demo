package com.gavin.business;

import com.gavin.common.messaging.CancelReservationProcessor;
import com.gavin.common.messaging.PaymentSucceededProcessor;
import com.gavin.common.messaging.RewardPointsProcessor;
import com.gavin.common.messaging.WaitingPaymentProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(scanBasePackages = {"com.gavin.business", "com.gavin.common"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = ("com.gavin.common.client"))
@EnableHystrix
@EnableCaching
@EnableBinding({
        RewardPointsProcessor.class,
        CancelReservationProcessor.class,
        WaitingPaymentProcessor.class,
        PaymentSucceededProcessor.class})
//@EnableSwagger2
public class OrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }

}
