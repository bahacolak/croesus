package com.bahadircolak.common.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserClient {

    private final RestTemplate restTemplate;

    @Value("${services.user-service.url:http://localhost:8081}")
    private String userServiceUrl;

    public Long getUserIdByUsername(String username) {
        try {
            String url = userServiceUrl + "/api/users/username/" + username + "/id";
            
            HttpHeaders headers = createAuthHeaders();
            RequestEntity<Void> request = new RequestEntity<>(headers, HttpMethod.GET, URI.create(url));
            ResponseEntity<Long> response = restTemplate.exchange(request, Long.class);
            
            return response.getBody();
        } catch (Exception e) {
            log.error("Error fetching user ID for username: {}", username, e);
            throw new RuntimeException("User not found: " + username);
        }
    }

    public Map<String, Object> getUserById(Long userId) {
        try {
            String url = userServiceUrl + "/api/users/" + userId;
            
            HttpHeaders headers = createAuthHeaders();
            RequestEntity<Void> request = new RequestEntity<>(headers, HttpMethod.GET, URI.create(url));
            ResponseEntity<Map> response = restTemplate.exchange(request, Map.class);
            
            return response.getBody();
        } catch (Exception e) {
            log.error("Error fetching user by ID: {}", userId, e);
            throw new RuntimeException("User not found: " + userId);
        }
    }

    public boolean userExists(Long userId) {
        try {
            getUserById(userId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public BigDecimal getUserWalletBalance(Long userId) {
        try {
            String url = userServiceUrl + "/api/users/" + userId + "/wallet/balance";
            
            HttpHeaders headers = createAuthHeaders();
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
            
            HttpHeaders headers = createAuthHeaders();
            RequestEntity<Void> request = new RequestEntity<>(headers, HttpMethod.POST, URI.create(url));
            restTemplate.exchange(request, Void.class);
            
        } catch (Exception e) {
            log.error("Error updating wallet balance for user: {} with amount: {}", userId, amount, e);
            throw new RuntimeException("Error updating wallet balance for user: " + userId);
        }
    }

    private HttpHeaders createAuthHeaders() {
        HttpHeaders headers = new HttpHeaders();
        String authHeader = (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        if (authHeader != null) {
            headers.set("Authorization", "Bearer " + authHeader);
        }
        return headers;
    }
} 