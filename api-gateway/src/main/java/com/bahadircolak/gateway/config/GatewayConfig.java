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
                
                .route("user-service-with-prefix", r -> r
                        .path("/user-service/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://user-service"))
                
                .route("portfolio-service", r -> r
                        .path("/api/portfolio/**", "/api/assets/**")
                        .uri("lb://portfolio-service"))
                
                .route("portfolio-service-with-prefix", r -> r
                        .path("/portfolio-service/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://portfolio-service"))
                
                .route("trading-service", r -> r
                        .path("/api/trading/**", "/api/transactions/**")
                        .uri("lb://trading-service"))
                
                .route("trading-service-with-prefix", r -> r
                        .path("/trading-service/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://trading-service"))
                
                .route("market-service", r -> r
                        .path("/api/market/**", "/api/prices/**", "/api/crypto/**")
                        .uri("lb://market-service"))
                
                .route("market-service-with-prefix", r -> r
                        .path("/market-service/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://market-service"))
                
                .route("wallet-service", r -> r
                        .path("/api/wallet/**")
                        .uri("lb://wallet-service"))
                
                .route("wallet-service-with-prefix", r -> r
                        .path("/wallet-service/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://wallet-service"))
                
                .route("health-check", r -> r
                        .path("/health")
                        .uri("lb://eureka-server"))
                
                .build();
    }
} 