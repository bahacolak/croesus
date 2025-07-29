package com.bahadircolak.market.service;

import com.bahadircolak.market.constants.ErrorMessages;
import com.bahadircolak.market.constants.MarketConstants;
import com.bahadircolak.market.dto.CoinGeckoResponseDto;
import com.bahadircolak.market.exception.AssetNotFoundException;
import com.bahadircolak.market.exception.MarketDataException;
import com.bahadircolak.market.model.CryptoCurrency;
import com.bahadircolak.market.repository.CryptoCurrencyRepository;
import lombok.RequiredArgsConstructor;
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

@Service
@RequiredArgsConstructor
public class CryptoServiceImpl implements CryptoService {

    private final RestTemplate restTemplate;
    private final CryptoCurrencyRepository repository;
    
    @Value("${coingecko.api.url}")
    private String coingeckoApiUrl;

    @Override
    public List<CryptoCurrency> fetchAndSaveLatestPrices() {
        try {
            String apiUrl = buildCoinGeckoApiUrl();
            List<CoinGeckoResponseDto> coinGeckoData = fetchDataFromCoinGecko(apiUrl);
            validateApiResponse(coinGeckoData);
            
            List<CryptoCurrency> cryptoCurrencies = convertToEntities(coinGeckoData);
            return saveCryptoCurrencies(cryptoCurrencies);
        } catch (MarketDataException e) {
            throw e;
        } catch (Exception e) {
            throw new MarketDataException(ErrorMessages.CRYPTOCURRENCY_UPDATE_FAILED, e);
        }
    }

    @Override
    public List<CryptoCurrency> getAllCryptoCurrencies() {
        return repository.findAllOrderByMarketCapDesc();
    }

    @Override
    public Optional<CryptoCurrency> getCryptoCurrencyBySymbol(String symbol) {
        return repository.findBySymbol(symbol.toUpperCase());
    }

    @Override
    public Optional<CryptoCurrency> getCryptoCurrencyById(String coinId) {
        return repository.findByCoinId(coinId);
    }

    @Override
    public List<CryptoCurrency> searchCryptoCurrencies(String query) {
        return repository.findByNameContainingIgnoreCase(query);
    }

    @Override
    public List<CryptoCurrency> getTopGainers() {
        return repository.findTopGainers();
    }

    @Override
    public List<CryptoCurrency> getTopLosers() {
        return repository.findTopLosers();
    }

    @Override
    public BigDecimal getCryptoPriceBySymbol(String symbol) {
        Optional<CryptoCurrency> crypto = getCryptoCurrencyBySymbol(symbol);
        return crypto.map(CryptoCurrency::getCurrentPrice)
                .orElseThrow(() -> new AssetNotFoundException(
                    String.format(ErrorMessages.PRICE_NOT_AVAILABLE, symbol)
                ));
    }

    private String buildCoinGeckoApiUrl() {
        return String.format("%s/coins/markets?vs_currency=%s&order=%s&per_page=%d&page=%d&sparkline=%s",
            coingeckoApiUrl,
            MarketConstants.COINGECKO_VS_CURRENCY,
            MarketConstants.COINGECKO_ORDER,
            MarketConstants.COINGECKO_DEFAULT_PER_PAGE,
            MarketConstants.COINGECKO_DEFAULT_PAGE,
            MarketConstants.COINGECKO_SPARKLINE
        );
    }

    private List<CoinGeckoResponseDto> fetchDataFromCoinGecko(String apiUrl) {
        try {
            ResponseEntity<List<CoinGeckoResponseDto>> responseEntity = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<CoinGeckoResponseDto>>() {}
            );
            return responseEntity.getBody();
        } catch (Exception e) {
            throw new MarketDataException(ErrorMessages.COINGECKO_API_ERROR, e);
        }
    }

    private void validateApiResponse(List<CoinGeckoResponseDto> coinGeckoData) {
        if (coinGeckoData == null || coinGeckoData.isEmpty()) {
            throw new MarketDataException(ErrorMessages.COINGECKO_NO_DATA);
        }
    }

    private List<CryptoCurrency> convertToEntities(List<CoinGeckoResponseDto> coinGeckoData) {
        return coinGeckoData.stream()
                .map(this::convertToCryptoCurrency)
                .collect(Collectors.toList());
    }

    private List<CryptoCurrency> saveCryptoCurrencies(List<CryptoCurrency> cryptoCurrencies) {
        return repository.saveAll(cryptoCurrencies);
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
        currency.setLastUpdated(parseLastUpdatedDate(dto.getLastUpdated()));
        return currency;
    }

    private LocalDateTime parseLastUpdatedDate(String lastUpdated) {
        if (lastUpdated == null) {
            return LocalDateTime.now();
        }
        
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
            return LocalDateTime.parse(lastUpdated, formatter);
        } catch (Exception e) {
            return LocalDateTime.now();
        }
    }
} 