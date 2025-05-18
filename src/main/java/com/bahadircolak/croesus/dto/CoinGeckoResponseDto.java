package com.bahadircolak.croesus.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CoinGeckoResponseDto {
    private String id;
    private String symbol;
    private String name;
    
    @JsonProperty("current_price")
    private Double currentPrice;
    
    @JsonProperty("market_cap")
    private Double marketCap;
    
    @JsonProperty("price_change_percentage_24h")
    private Double priceChangePercentage24h;
    
    @JsonProperty("last_updated")
    private String lastUpdated;
    
    @JsonProperty("image")
    private String imageUrl;
} 