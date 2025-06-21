package com.bahadircolak.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication(exclude = {org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class})
@EnableEurekaServer
public class EurekaServerApplication {

    public static void main(String[] args) {
        System.out.println("🚀 Eureka Server starting...");
        SpringApplication.run(EurekaServerApplication.class, args);
        System.out.println("✅ Eureka Server started successfully!");
        System.out.println("📊 Dashboard: http://localhost:8761");
    }
} 