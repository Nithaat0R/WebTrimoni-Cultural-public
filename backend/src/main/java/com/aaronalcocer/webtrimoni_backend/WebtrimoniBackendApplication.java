package com.aaronalcocer.webtrimoni_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class WebtrimoniBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebtrimoniBackendApplication.class, args);
    }
}
