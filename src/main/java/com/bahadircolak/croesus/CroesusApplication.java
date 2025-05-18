package com.bahadircolak.croesus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CroesusApplication {

    public static void main(String[] args) {
        SpringApplication.run(CroesusApplication.class, args);
    }
} 