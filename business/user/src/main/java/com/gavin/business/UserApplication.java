package com.gavin.business;

import com.gavin.common.messaging.UserActivatedProcessor;
import com.gavin.common.messaging.UserCreatedProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(scanBasePackages = {"com.gavin.business", "com.gavin.common"})
@EnableDiscoveryClient
@EnableBinding({UserCreatedProcessor.class, UserActivatedProcessor.class})
@EnableJpaRepositories(basePackages = "com.gavin.business.repository")
@EnableSwagger2
public class UserApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }

}
