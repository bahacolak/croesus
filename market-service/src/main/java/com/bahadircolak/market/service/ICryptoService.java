package com.bahadircolak.market.service;

import com.bahadircolak.market.model.CryptoCurrency;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ICryptoService {
    
    List<CryptoCurrency> fetchAndSaveLatestPrices();
    
    List<CryptoCurrency> getAllCryptoCurrencies();
    
    Optional<CryptoCurrency> getCryptoCurrencyBySymbol(String symbol);
    
    Optional<CryptoCurrency> getCryptoCurrencyById(String coinId);
    
    List<CryptoCurrency> searchCryptoCurrencies(String query);
    
    List<CryptoCurrency> getTopGainers();
    
    List<CryptoCurrency> getTopLosers();
    
    BigDecimal getCryptoPriceBySymbol(String symbol);
} 