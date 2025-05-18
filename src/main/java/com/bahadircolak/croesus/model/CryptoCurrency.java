package com.bahadircolak.croesus.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CryptoCurrency {
    @Id
    private String symbol;
    
    private String name;
    private String coinId;
    private Double currentPrice;
    private Double marketCap;
    private Double priceChangePercent24h;
    private LocalDateTime lastUpdated;
    private String imageUrl;
} 