package com.bahadircolak.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user-service", r -> r
                        .path("/api/users/**", "/api/auth/**")
                        .uri("lb://user-service"))
                
                .route("portfolio-service", r -> r
                        .path("/api/portfolios/**", "/api/assets/**")
                        .uri("lb://portfolio-service"))
                
                .route("trading-service", r -> r
                        .path("/api/trades/**", "/api/orders/**")
                        .uri("lb://trading-service"))
                
                .route("market-service", r -> r
                        .path("/api/market/**", "/api/prices/**", "/api/crypto/**")
                        .uri("lb://market-service"))
                
                .route("wallet-service", r -> r
                        .path("/api/wallet/**", "/api/transactions/**")
                        .uri("lb://wallet-service"))
                
                .route("health-check", r -> r
                        .path("/health")
                        .uri("lb://eureka-server"))
                
                .build();
    }
} 