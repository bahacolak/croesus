package com.bahadircolak.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * Config Server - Central Configuration Management
 * Manages configuration for all microservices
 * 
 * Port: 8888
 * Config Repository: classpath:/config-repo
 * 
 * Note: @EnableEurekaClient is no longer required, it's automatically active
 */
@SpringBootApplication
@EnableConfigServer
public class ConfigServerApplication {

    public static void main(String[] args) {
        System.out.println("⚙️ Config Server starting...");
        SpringApplication.run(ConfigServerApplication.class, args);
        System.out.println("✅ Config Server started successfully!");
        System.out.println("🔧 Config URL: http://localhost:8888");
    }
} 