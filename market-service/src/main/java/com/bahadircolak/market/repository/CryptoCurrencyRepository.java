package com.bahadircolak.market.repository;

import com.bahadircolak.market.model.CryptoCurrency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CryptoCurrencyRepository extends JpaRepository<CryptoCurrency, String> {
    
    Optional<CryptoCurrency> findBySymbol(String symbol);
    
    Optional<CryptoCurrency> findByCoinId(String coinId);
    
    List<CryptoCurrency> findByNameContainingIgnoreCase(String name);
    
    @Query("SELECT c FROM CryptoCurrency c ORDER BY c.marketCap DESC")
    List<CryptoCurrency> findAllOrderByMarketCapDesc();
    
    @Query("SELECT c FROM CryptoCurrency c ORDER BY c.priceChangePercent24h DESC")
    List<CryptoCurrency> findTopGainers();
    
    @Query("SELECT c FROM CryptoCurrency c ORDER BY c.priceChangePercent24h ASC")
    List<CryptoCurrency> findTopLosers();
} 