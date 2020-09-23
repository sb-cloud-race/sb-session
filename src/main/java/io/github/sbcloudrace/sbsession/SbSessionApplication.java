package io.github.sbcloudrace.sbsession;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class SbSessionApplication {

    public static void main(String[] args) {
        SpringApplication.run(SbSessionApplication.class, args);
    }

}
