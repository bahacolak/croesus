package com.bahadircolak.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * API Gateway - Tüm mikroservislere giriş noktası
 * JWT authentication, rate limiting, routing
 * 
 * Port: 8080
 * Routes:
 * - /api/users/** -> user-service
 * - /api/portfolios/** -> portfolio-service  
 * - /api/trades/** -> trading-service
 * - /api/market/** -> market-service
 * 
 * Not: @EnableEurekaClient artık gerekli değil, otomatik olarak aktif
 */
@SpringBootApplication
public class ApiGatewayApplication {

    public static void main(String[] args) {
        System.out.println("🚪 API Gateway başlatılıyor...");
        SpringApplication.run(ApiGatewayApplication.class, args);
        System.out.println("✅ API Gateway başarıyla başlatıldı!");
        System.out.println("🌐 Gateway URL: http://localhost:8080");
    }
} 