package com.bahadircolak.market.service;

import com.bahadircolak.market.constants.ErrorMessages;
import com.bahadircolak.market.constants.MarketConstants;
import com.bahadircolak.market.dto.response.AssetResponse;
import com.bahadircolak.market.exception.AssetNotFoundException;
import com.bahadircolak.market.model.CryptoCurrency;
import com.bahadircolak.market.validation.MarketValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MarketServiceImpl implements MarketService {

    private final CryptoService cryptoService;
    private final MarketValidator validator;

    @Override
    public List<AssetResponse> getAllAssets() {
        List<CryptoCurrency> cryptos = cryptoService.getAllCryptoCurrencies();
        return convertToAssetResponses(cryptos);
    }

    @Override
    public List<AssetResponse> getMarketOverview() {
        List<CryptoCurrency> cryptos = cryptoService.getAllCryptoCurrencies();
        return cryptos.stream()
                .limit(MarketConstants.DEFAULT_MARKET_OVERVIEW_LIMIT)
                .map(this::convertToAssetResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<AssetResponse> getAssetBySymbol(String symbol) {
        validator.validateSymbol(symbol);
        Optional<CryptoCurrency> crypto = cryptoService.getCryptoCurrencyBySymbol(symbol);
        return crypto.map(this::convertToAssetResponse);
    }

    @Override
    public Optional<AssetResponse> getAssetById(String coinId) {
        validator.validateCoinId(coinId);
        Optional<CryptoCurrency> crypto = cryptoService.getCryptoCurrencyById(coinId);
        return crypto.map(this::convertToAssetResponse);
    }

    @Override
    public BigDecimal getAssetPrice(String symbol) {
        validator.validateSymbol(symbol);
        return cryptoService.getCryptoPriceBySymbol(symbol);
    }

    @Override
    public List<AssetResponse> searchAssets(String query) {
        validator.validateSearchQuery(query);
        List<CryptoCurrency> cryptos = cryptoService.searchCryptoCurrencies(query);
        return convertToAssetResponses(cryptos);
    }

    @Override
    public List<AssetResponse> getTopGainers() {
        List<CryptoCurrency> cryptos = cryptoService.getTopGainers();
        return cryptos.stream()
                .limit(MarketConstants.DEFAULT_TOP_GAINERS_LIMIT)
                .map(this::convertToAssetResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<AssetResponse> getTopLosers() {
        List<CryptoCurrency> cryptos = cryptoService.getTopLosers();
        return cryptos.stream()
                .limit(MarketConstants.DEFAULT_TOP_LOSERS_LIMIT)
                .map(this::convertToAssetResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<AssetResponse> refreshMarketData() {
        List<CryptoCurrency> updatedCryptos = cryptoService.fetchAndSaveLatestPrices();
        return convertToAssetResponses(updatedCryptos);
    }

    private List<AssetResponse> convertToAssetResponses(List<CryptoCurrency> cryptos) {
        return cryptos.stream()
                .map(this::convertToAssetResponse)
                .collect(Collectors.toList());
    }

    private AssetResponse convertToAssetResponse(CryptoCurrency crypto) {
        AssetResponse response = new AssetResponse();
        response.setId(generateAssetId(crypto.getSymbol()));
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

    private Long generateAssetId(String symbol) {
        return Long.valueOf(Math.abs(symbol.hashCode()));
    }
} 