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

    public void updatePortfolio(Long userId, Long assetId, BigDecimal quantity, BigDecimal price, String action, String assetSymbol, String assetName) {
        try {
            String url = portfolioServiceUrl + "/api/portfolio/user/" + userId + "/asset/" + assetId + "/update";
            
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            String authHeader = (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();
            if (authHeader != null) {
                headers.set("Authorization", "Bearer " + authHeader);
            }

            Map<String, Object> requestBody = Map.of(
                "quantity", quantity.toString(),
                "price", price.toString(),
                "action", action,
                "assetSymbol", assetSymbol,
                "assetName", assetName
            );

            RequestEntity<Map<String, Object>> request = new RequestEntity<>(requestBody, headers, HttpMethod.POST, URI.create(url));
            restTemplate.exchange(request, Map.class);
            
            log.info("Portfolio updated for user: {}, asset: {}, action: {}", userId, assetId, action);
        } catch (Exception e) {
            log.error("Error updating portfolio for user: {} and asset: {}", userId, assetId, e);
            throw new RuntimeException("Failed to update portfolio: " + e.getMessage());
        }
    }
} 