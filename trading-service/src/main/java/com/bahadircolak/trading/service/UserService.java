package com.bahadircolak.trading.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final RestTemplate restTemplate;

    @Value("${services.user-service.url:http://localhost:8081}")
    private String userServiceUrl;

    public Long getUserIdByUsername(String username) {
        try {
            String url = userServiceUrl + "/api/users/username/" + username;
            
            HttpHeaders headers = new HttpHeaders();
            String authHeader = (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();
            if (authHeader != null) {
                headers.set("Authorization", "Bearer " + authHeader);
            }

            RequestEntity<Void> request = new RequestEntity<>(headers, HttpMethod.GET, URI.create(url));
            ResponseEntity<Map> response = restTemplate.exchange(request, Map.class);
            
            if (response.getBody() != null) {
                return Long.valueOf(response.getBody().get("id").toString());
            }
        } catch (Exception e) {
            log.error("Error fetching user ID for username: {}", username, e);
        }
        
        throw new RuntimeException("User not found: " + username);
    }

    public BigDecimal getUserWalletBalance(Long userId) {
        try {
            String url = userServiceUrl + "/api/users/" + userId + "/wallet/balance";
            
            HttpHeaders headers = new HttpHeaders();
            String authHeader = (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();
            if (authHeader != null) {
                headers.set("Authorization", "Bearer " + authHeader);
            }

            RequestEntity<Void> request = new RequestEntity<>(headers, HttpMethod.GET, URI.create(url));
            ResponseEntity<BigDecimal> response = restTemplate.exchange(request, BigDecimal.class);
            
            return response.getBody();
        } catch (Exception e) {
            log.error("Error fetching wallet balance for user: {}", userId, e);
            throw new RuntimeException("Error fetching wallet balance for user: " + userId);
        }
    }

    public void updateUserWalletBalance(Long userId, BigDecimal amount) {
        try {
            String url = userServiceUrl + "/api/users/" + userId + "/wallet/update?amount=" + amount;
            
            HttpHeaders headers = new HttpHeaders();
            String authHeader = (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();
            if (authHeader != null) {
                headers.set("Authorization", "Bearer " + authHeader);
            }

            RequestEntity<Void> request = new RequestEntity<>(headers, HttpMethod.POST, URI.create(url));
            restTemplate.exchange(request, Void.class);
            
        } catch (Exception e) {
            log.error("Error updating wallet balance for user: {} with amount: {}", userId, amount, e);
            throw new RuntimeException("Error updating wallet balance for user: " + userId);
        }
    }
} 