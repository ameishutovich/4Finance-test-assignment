package com.meishuto.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ScopedProxyMode;

@Configuration
@ComponentScan(basePackages = "com.meishuto", scopedProxy = ScopedProxyMode.INTERFACES)
@EnableAutoConfiguration
public class RestApiApplication {

    public static void main(final String... args) {
        SpringApplication.run(RestApiApplication.class, args);
    }
}