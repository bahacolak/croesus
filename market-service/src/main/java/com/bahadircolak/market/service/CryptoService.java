package com.bahadircolak.market.service;

import com.bahadircolak.market.dto.CoinGeckoResponseDto;
import com.bahadircolak.market.model.CryptoCurrency;
import com.bahadircolak.market.repository.CryptoCurrencyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CryptoService {

    private final RestTemplate restTemplate;
    private final CryptoCurrencyRepository repository;
    
    @Value("${coingecko.api.url}")
    private String coingeckoApiUrl;

    public List<CryptoCurrency> fetchAndSaveLatestPrices() {
        try {
            String url = coingeckoApiUrl + "/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=100&page=1&sparkline=false";
            
            log.info("Fetching cryptocurrency data from: {}", url);
            
            ResponseEntity<List<CoinGeckoResponseDto>> responseEntity = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<CoinGeckoResponseDto>>() {}
            );

            List<CoinGeckoResponseDto> coinGeckoData = responseEntity.getBody();
            
            if (coinGeckoData == null || coinGeckoData.isEmpty()) {
                log.warn("No data received from CoinGecko API");
                return List.of();
            }

            List<CryptoCurrency> cryptoCurrencies = coinGeckoData.stream()
                    .map(this::convertToCryptoCurrency)
                    .collect(Collectors.toList());

            List<CryptoCurrency> savedCryptos = repository.saveAll(cryptoCurrencies);
            log.info("Successfully updated {} cryptocurrencies", savedCryptos.size());
            
            return savedCryptos;
        } catch (Exception e) {
            log.error("Error fetching cryptocurrency data from CoinGecko", e);
            throw new RuntimeException("Failed to fetch cryptocurrency data: " + e.getMessage());
        }
    }

    private CryptoCurrency convertToCryptoCurrency(CoinGeckoResponseDto dto) {
        CryptoCurrency currency = new CryptoCurrency();
        currency.setSymbol(dto.getSymbol().toUpperCase());
        currency.setName(dto.getName());
        currency.setCoinId(dto.getId());
        currency.setCurrentPrice(dto.getCurrentPrice());
        currency.setMarketCap(dto.getMarketCap());
        currency.setPriceChange24h(dto.getPriceChange24h());
        currency.setPriceChangePercent24h(dto.getPriceChangePercentage24h());
        currency.setTotalVolume(dto.getTotalVolume());
        currency.setCirculatingSupply(dto.getCirculatingSupply());
        currency.setTotalSupply(dto.getTotalSupply());
        currency.setImageUrl(dto.getImageUrl());
        
        if (dto.getLastUpdated() != null) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
                currency.setLastUpdated(LocalDateTime.parse(dto.getLastUpdated(), formatter));
            } catch (Exception e) {
                log.warn("Failed to parse last updated date: {}", dto.getLastUpdated());
                currency.setLastUpdated(LocalDateTime.now());
            }
        } else {
            currency.setLastUpdated(LocalDateTime.now());
        }
        
        return currency;
    }

    public List<CryptoCurrency> getAllCryptoCurrencies() {
        return repository.findAllOrderByMarketCapDesc();
    }

    public Optional<CryptoCurrency> getCryptoCurrencyBySymbol(String symbol) {
        return repository.findBySymbol(symbol.toUpperCase());
    }

    public Optional<CryptoCurrency> getCryptoCurrencyById(String coinId) {
        return repository.findByCoinId(coinId);
    }

    public List<CryptoCurrency> searchCryptoCurrencies(String query) {
        return repository.findByNameContainingIgnoreCase(query);
    }

    public List<CryptoCurrency> getTopGainers() {
        return repository.findTopGainers();
    }

    public List<CryptoCurrency> getTopLosers() {
        return repository.findTopLosers();
    }

    public BigDecimal getCryptoPriceBySymbol(String symbol) {
        Optional<CryptoCurrency> crypto = getCryptoCurrencyBySymbol(symbol);
        if (crypto.isPresent()) {
            return crypto.get().getCurrentPrice();
        }
        throw new RuntimeException("Cryptocurrency not found: " + symbol);
    }
} 