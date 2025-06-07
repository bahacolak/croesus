package com.bahadircolak.wallet.service;

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
            String url = userServiceUrl + "/api/users/username/" + username + "/id";
            
            HttpHeaders headers = new HttpHeaders();
            String authHeader = (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();
            if (authHeader != null) {
                headers.set("Authorization", "Bearer " + authHeader);
            }

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
            
            HttpHeaders headers = new HttpHeaders();
            String authHeader = (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();
            if (authHeader != null) {
                headers.set("Authorization", "Bearer " + authHeader);
            }

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
} 