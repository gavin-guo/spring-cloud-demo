package com.gavin;

import com.gavin.messaging.PaymentSucceededProcessor;
import com.gavin.messaging.WaitingForPaymentProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.stream.annotation.EnableBinding;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableDiscoveryClient
@EnableBinding({
        WaitingForPaymentProcessor.class,
        PaymentSucceededProcessor.class})
@EnableSwagger2
public class PaymentApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaymentApplication.class, args);
    }

}
