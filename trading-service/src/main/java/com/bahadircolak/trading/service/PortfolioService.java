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

import java.net.URI;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final RestTemplate restTemplate;

    @Value("${services.portfolio-service.url:http://localhost:8082}")
    private String portfolioServiceUrl;

    public Map<String, Object> getPortfolioByUserAndAsset(Long userId, Long assetId) {
        try {
            String url = portfolioServiceUrl + "/api/portfolio/user/" + userId + "/asset/" + assetId;
            
            HttpHeaders headers = new HttpHeaders();
            String authHeader = (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();
            if (authHeader != null) {
                headers.set("Authorization", "Bearer " + authHeader);
            }

            RequestEntity<Void> request = new RequestEntity<>(headers, HttpMethod.GET, URI.create(url));
            ResponseEntity<Map> response = restTemplate.exchange(request, Map.class);
            
            return response.getBody();
        } catch (Exception e) {
            log.debug("Portfolio not found for user: {} and asset: {}", userId, assetId);
            return null;
        }
    }
} 