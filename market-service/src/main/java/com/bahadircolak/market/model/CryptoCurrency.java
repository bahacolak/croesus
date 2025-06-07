package com.bahadircolak.market.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cryptocurrencies")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CryptoCurrency {
    @Id
    private String symbol;
    
    @Column(nullable = false)
    private String name;
    
    @Column(name = "coin_id", unique = true)
    private String coinId;
    
    @Column(name = "current_price", precision = 18, scale = 8)
    private BigDecimal currentPrice;
    
    @Column(name = "market_cap")
    private Long marketCap;
    
    @Column(name = "price_change_24h", precision = 18, scale = 8)
    private BigDecimal priceChange24h;
    
    @Column(name = "price_change_percent_24h", precision = 5, scale = 2)
    private BigDecimal priceChangePercent24h;
    
    @Column(name = "total_volume")
    private Long totalVolume;
    
    @Column(name = "circulating_supply", precision = 20, scale = 2)
    private BigDecimal circulatingSupply;
    
    @Column(name = "total_supply", precision = 20, scale = 2)
    private BigDecimal totalSupply;
    
    @Column(name = "image_url")
    private String imageUrl;
    
    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;
} 