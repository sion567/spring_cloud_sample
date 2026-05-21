package com.shop.gateway;

import com.shop.gateway.filter.GatewayLogFilter;
import com.shop.gateway.filter.GatewayLogFilter2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

import java.util.Locale;

@SpringBootApplication
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @Bean
    public GlobalFilter customFilter() {
        return new GatewayLogFilter();
    }

    @Bean
    public GatewayFilter customFilter2() {
        return new GatewayLogFilter2();
    }

}
