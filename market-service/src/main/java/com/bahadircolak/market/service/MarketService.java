package com.bahadircolak.market.service;

import com.bahadircolak.market.dto.response.AssetResponse;
import com.bahadircolak.market.model.CryptoCurrency;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MarketService {

    private final CryptoService cryptoService;

    public List<AssetResponse> getAllAssets() {
        List<CryptoCurrency> cryptos = cryptoService.getAllCryptoCurrencies();
        return cryptos.stream()
                .map(this::convertToAssetResponse)
                .collect(Collectors.toList());
    }

    public Optional<AssetResponse> getAssetBySymbol(String symbol) {
        Optional<CryptoCurrency> crypto = cryptoService.getCryptoCurrencyBySymbol(symbol);
        return crypto.map(this::convertToAssetResponse);
    }

    public Optional<AssetResponse> getAssetById(String coinId) {
        Optional<CryptoCurrency> crypto = cryptoService.getCryptoCurrencyById(coinId);
        return crypto.map(this::convertToAssetResponse);
    }

    public BigDecimal getAssetPrice(String symbol) {
        return cryptoService.getCryptoPriceBySymbol(symbol);
    }

    public List<AssetResponse> searchAssets(String query) {
        List<CryptoCurrency> cryptos = cryptoService.searchCryptoCurrencies(query);
        return cryptos.stream()
                .map(this::convertToAssetResponse)
                .collect(Collectors.toList());
    }

    public List<AssetResponse> getTopGainers() {
        List<CryptoCurrency> cryptos = cryptoService.getTopGainers();
        return cryptos.stream()
                .map(this::convertToAssetResponse)
                .collect(Collectors.toList());
    }

    public List<AssetResponse> getTopLosers() {
        List<CryptoCurrency> cryptos = cryptoService.getTopLosers();
        return cryptos.stream()
                .map(this::convertToAssetResponse)
                .collect(Collectors.toList());
    }

    public List<AssetResponse> refreshMarketData() {
        List<CryptoCurrency> updatedCryptos = cryptoService.fetchAndSaveLatestPrices();
        return updatedCryptos.stream()
                .map(this::convertToAssetResponse)
                .collect(Collectors.toList());
    }

    private AssetResponse convertToAssetResponse(CryptoCurrency crypto) {
        AssetResponse response = new AssetResponse();
        response.setId(null); // CryptoCurrency uses symbol as ID
        response.setSymbol(crypto.getSymbol());
        response.setName(crypto.getName());
        response.setCoinId(crypto.getCoinId());
        response.setCurrentPrice(crypto.getCurrentPrice());
        response.setMarketCap(crypto.getMarketCap());
        response.setPriceChange24h(crypto.getPriceChange24h());
        response.setPriceChangePercent24h(crypto.getPriceChangePercent24h());
        response.setTotalVolume(crypto.getTotalVolume());
        response.setCirculatingSupply(crypto.getCirculatingSupply());
        response.setTotalSupply(crypto.getTotalSupply());
        response.setImageUrl(crypto.getImageUrl());
        response.setLastUpdated(crypto.getLastUpdated());
        return response;
    }
} 