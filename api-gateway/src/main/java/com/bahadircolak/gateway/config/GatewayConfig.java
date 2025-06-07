package com.bahadircolak.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Gateway Routing Configuration
 * Determines which path goes to which service
 */
@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                // User Service Routes - User management
                .route("user-service", r -> r
                        .path("/api/users/**", "/api/auth/**")
                        .uri("lb://user-service"))
                
                // Portfolio Service Routes - Portfolio management
                .route("portfolio-service", r -> r
                        .path("/api/portfolios/**", "/api/assets/**")
                        .uri("lb://portfolio-service"))
                
                // Trading Service Routes - Trading operations
                .route("trading-service", r -> r
                        .path("/api/trades/**", "/api/orders/**")
                        .uri("lb://trading-service"))
                
                // Market Service Routes - Market data
                .route("market-service", r -> r
                        .path("/api/market/**", "/api/prices/**")
                        .uri("lb://market-service"))
                
                // Health Check Route - System status
                .route("health-check", r -> r
                        .path("/health")
                        .uri("lb://eureka-server"))
                
                .build();
    }
} 