package com.bahadircolak.market.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssetResponse {
    private Long id;
    private String symbol;
    private String name;
    private String coinId;
    private BigDecimal currentPrice;
    private Long marketCap;
    private BigDecimal priceChange24h;
    private BigDecimal priceChangePercent24h;
    private Long totalVolume;
    private BigDecimal circulatingSupply;
    private BigDecimal totalSupply;
    private String imageUrl;
    private LocalDateTime lastUpdated;
} 