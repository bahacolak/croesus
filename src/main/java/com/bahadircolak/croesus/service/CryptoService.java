package com.bahadircolak.croesus.service;

import com.bahadircolak.croesus.dto.CoinGeckoResponseDto;
import com.bahadircolak.croesus.model.CryptoCurrency;
import com.bahadircolak.croesus.repository.CryptoCurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CryptoService {

    private final RestTemplate restTemplate;
    private final CryptoCurrencyRepository repository;
    
    @Value("${app.api.coingecko.url}")
    private String coingeckoApiUrl;
    
    @Value("${app.api.coingecko.default-params}")
    private String coingeckoDefaultParams;

    @Autowired
    public CryptoService(RestTemplate restTemplate, CryptoCurrencyRepository repository) {
        this.restTemplate = restTemplate;
        this.repository = repository;
    }

    public List<CryptoCurrency> fetchAndSaveLatestPrices() {
        String fullApiUrl = coingeckoApiUrl + coingeckoDefaultParams;
        
        ResponseEntity<List<CoinGeckoResponseDto>> responseEntity = restTemplate.exchange(
                fullApiUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<CoinGeckoResponseDto>>() {}
        );

        List<CoinGeckoResponseDto> coinGeckoData = responseEntity.getBody();
        
        if (coinGeckoData == null) {
            return List.of();
        }

        List<CryptoCurrency> cryptoCurrencies = coinGeckoData.stream()
                .map(this::convertToCryptoCurrency)
                .collect(Collectors.toList());

        return repository.saveAll(cryptoCurrencies);
    }

    private CryptoCurrency convertToCryptoCurrency(CoinGeckoResponseDto dto) {
        CryptoCurrency currency = new CryptoCurrency();
        currency.setSymbol(dto.getSymbol().toUpperCase());
        currency.setName(dto.getName());
        currency.setCoinId(dto.getId());
        currency.setCurrentPrice(dto.getCurrentPrice());
        currency.setMarketCap(dto.getMarketCap());
        currency.setPriceChangePercent24h(dto.getPriceChangePercentage24h());
        currency.setImageUrl(dto.getImageUrl());
        
        if (dto.getLastUpdated() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
            currency.setLastUpdated(LocalDateTime.parse(dto.getLastUpdated(), formatter));
        } else {
            currency.setLastUpdated(LocalDateTime.now());
        }
        
        return currency;
    }

    public List<CryptoCurrency> getAllCryptoCurrencies() {
        return repository.findAll();
    }
} 