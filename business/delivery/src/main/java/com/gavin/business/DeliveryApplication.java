package com.gavin.business;

import com.gavin.common.messaging.ArrangeShipmentProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.stream.annotation.EnableBinding;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(scanBasePackages = {"com.gavin.business", "com.gavin.common"})
@EnableDiscoveryClient
@EnableBinding(ArrangeShipmentProcessor.class)
@EnableSwagger2
public class DeliveryApplication {

    public static void main(String[] args) {
        SpringApplication.run(DeliveryApplication.class, args);
    }

}
