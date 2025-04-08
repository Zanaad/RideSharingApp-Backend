package com.ridesharingappbackend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI rideSharingOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Ride Sharing App API")
                        .description("API documentation for Ride Sharing Application")
                        .version("v1.0"));
    }
}
