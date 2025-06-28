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
            
            HttpHeaders headers = createAuthHeaders();
            RequestEntity<Void> request = new RequestEntity<>(headers, HttpMethod.GET, URI.create(url));
            ResponseEntity<Map> response = restTemplate.exchange(request, Map.class);
            
            return response.getBody();
        } catch (Exception e) {
            log.debug("Portfolio not found for user: {} and asset: {}", userId, assetId);
            return null;
        }
    }

    public Map<String, Object> getPortfolioByUserAndSymbol(Long userId, String symbol) {
        try {
            String url = portfolioServiceUrl + "/api/portfolio/user/" + userId + "/symbol/" + symbol;
            
            HttpHeaders headers = createAuthHeaders();
            RequestEntity<Void> request = new RequestEntity<>(headers, HttpMethod.GET, URI.create(url));
            ResponseEntity<Map> response = restTemplate.exchange(request, Map.class);
            
            return response.getBody();
        } catch (Exception e) {
            log.debug("Portfolio not found for user: {} and symbol: {}", userId, symbol);
            return null;
        }
    }

    public void updatePortfolio(Long userId, Long assetId, BigDecimal quantity, BigDecimal price, String action, String assetSymbol, String assetName) {
        try {
            String url = portfolioServiceUrl + "/api/portfolio/user/" + userId + "/asset/" + assetId + "/update";
            
            HttpHeaders headers = createAuthHeaders();
            headers.set("Content-Type", "application/json");

            // Debug logging
            log.info("Sending request to: {}", url);
            log.info("Authorization header: {}", headers.get("Authorization"));

            Map<String, Object> requestBody = Map.of(
                "quantity", quantity,
                "price", price,
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

    public Long getAssetIdBySymbol(String symbol) {
        try {
            String url = portfolioServiceUrl + "/api/portfolio/asset/symbol/" + symbol;
            
            HttpHeaders headers = createAuthHeaders();
            RequestEntity<Void> request = new RequestEntity<>(headers, HttpMethod.GET, URI.create(url));
            ResponseEntity<Map> response = restTemplate.exchange(request, Map.class);
            
            Map<String, Object> result = response.getBody();
            if (result != null && result.get("id") != null) {
                return Long.valueOf(result.get("id").toString());
            }
            return null;
        } catch (Exception e) {
            log.debug("Asset not found by symbol: {}", symbol);
            return null;
        }
    }

    private HttpHeaders createAuthHeaders() {
        HttpHeaders headers = new HttpHeaders();
        Object credentials = SecurityContextHolder.getContext().getAuthentication().getCredentials();
        if (credentials != null) {
            headers.set("Authorization", "Bearer " + credentials.toString());
        }
        return headers;
    }
} 