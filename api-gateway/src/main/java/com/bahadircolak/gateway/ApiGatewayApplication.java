package com.bahadircolak.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class ApiGatewayApplication {

    public static void main(String[] args) {
        System.out.println("🚪 API Gateway başlatılıyor...");
        SpringApplication.run(ApiGatewayApplication.class, args);
        System.out.println("✅ API Gateway başarıyla başlatıldı!");
        System.out.println("🌐 Gateway URL: http://localhost:8080");
    }
} 