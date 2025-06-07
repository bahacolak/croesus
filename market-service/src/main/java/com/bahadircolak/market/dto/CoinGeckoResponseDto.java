package com.bahadircolak.market.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoinGeckoResponseDto {
    private String id;
    private String symbol;
    private String name;
    
    @JsonProperty("current_price")
    private BigDecimal currentPrice;
    
    @JsonProperty("market_cap")
    private Long marketCap;
    
    @JsonProperty("price_change_percentage_24h")
    private BigDecimal priceChangePercentage24h;
    
    @JsonProperty("price_change_24h")
    private BigDecimal priceChange24h;
    
    @JsonProperty("last_updated")
    private String lastUpdated;
    
    @JsonProperty("image")
    private String imageUrl;
    
    @JsonProperty("total_volume")
    private Long totalVolume;
    
    @JsonProperty("circulating_supply")
    private BigDecimal circulatingSupply;
    
    @JsonProperty("total_supply")
    private BigDecimal totalSupply;
} 