package com.ridesharingappbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class RideSharingAppBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(RideSharingAppBackendApplication.class, args);
    }

}
