package com.bahadircolak.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

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