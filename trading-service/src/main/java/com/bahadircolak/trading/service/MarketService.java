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
public class MarketService {

    private final RestTemplate restTemplate;

    @Value("${services.market-service.url:http://localhost:8085}")
    private String marketServiceUrl;

    public Map<String, Object> getAssetBySymbol(String symbol) {
        try {
            String url = marketServiceUrl + "/api/assets/" + symbol;
            
            HttpHeaders headers = new HttpHeaders();
            String authHeader = (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();
            if (authHeader != null) {
                headers.set("Authorization", "Bearer " + authHeader);
            }

            RequestEntity<Void> request = new RequestEntity<>(headers, HttpMethod.GET, URI.create(url));
            ResponseEntity<Map> response = restTemplate.exchange(request, Map.class);
            
            return response.getBody();
        } catch (Exception e) {
            log.error("Error fetching asset: {}", symbol, e);
            throw new RuntimeException("Asset not found: " + symbol);
        }
    }

    public BigDecimal getAssetCurrentPrice(String symbol) {
        Map<String, Object> asset = getAssetBySymbol(symbol);
        if (asset != null && asset.get("currentPrice") != null) {
            return new BigDecimal(asset.get("currentPrice").toString());
        }
        throw new RuntimeException("Asset price not found: " + symbol);
    }

    public Long getAssetId(String symbol) {
        Map<String, Object> asset = getAssetBySymbol(symbol);
        if (asset != null && asset.get("id") != null) {
            return Long.valueOf(asset.get("id").toString());
        }
        throw new RuntimeException("Asset ID not found: " + symbol);
    }
} 