package com.bahadircolak.user.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.web.client.RestTemplate;

@Configuration
public class EurekaConfig {

    @Value("${eureka.client.eureka-server-username:admin}")
    private String username;

    @Value("${eureka.client.eureka-server-password:admin}")
    private String password;

    @Bean
    public RestTemplate eurekaRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(username, password));
        return restTemplate;
    }
} 