package com.bahadircolak.market.controller;

import com.bahadircolak.market.constants.ErrorMessages;
import com.bahadircolak.market.dto.response.MessageResponse;
import com.bahadircolak.market.exception.AssetNotFoundException;
import com.bahadircolak.market.model.CryptoCurrency;
import com.bahadircolak.market.service.ICryptoService;
import com.bahadircolak.market.validation.MarketValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/crypto")
@RequiredArgsConstructor
public class CryptoController {

    private final ICryptoService cryptoService;
    private final MarketValidator validator;

    @GetMapping
    public ResponseEntity<List<CryptoCurrency>> getAllCryptoCurrencies() {
        List<CryptoCurrency> cryptos = cryptoService.getAllCryptoCurrencies();
        return ResponseEntity.ok(cryptos);
    }

    @GetMapping("/{symbol}")
    public ResponseEntity<CryptoCurrency> getCryptoCurrencyBySymbol(@PathVariable String symbol) {
        validator.validateSymbol(symbol);
        Optional<CryptoCurrency> crypto = cryptoService.getCryptoCurrencyBySymbol(symbol);
        return crypto.map(ResponseEntity::ok)
                .orElseThrow(() -> new AssetNotFoundException(
                    String.format(ErrorMessages.ASSET_NOT_FOUND_BY_SYMBOL, symbol)
                ));
    }

    @GetMapping("/{symbol}/price")
    public ResponseEntity<BigDecimal> getCryptoPriceBySymbol(@PathVariable String symbol) {
        validator.validateSymbol(symbol);
        BigDecimal price = cryptoService.getCryptoPriceBySymbol(symbol);
        return ResponseEntity.ok(price);
    }

    @GetMapping("/search")
    public ResponseEntity<List<CryptoCurrency>> searchCryptoCurrencies(@RequestParam("q") String query) {
        validator.validateSearchQuery(query);
        List<CryptoCurrency> cryptos = cryptoService.searchCryptoCurrencies(query);
        return ResponseEntity.ok(cryptos);
    }

    @GetMapping("/gainers")
    public ResponseEntity<List<CryptoCurrency>> getTopGainers() {
        List<CryptoCurrency> gainers = cryptoService.getTopGainers();
        return ResponseEntity.ok(gainers);
    }

    @GetMapping("/losers")
    public ResponseEntity<List<CryptoCurrency>> getTopLosers() {
        List<CryptoCurrency> losers = cryptoService.getTopLosers();
        return ResponseEntity.ok(losers);
    }

    @PostMapping("/refresh")
    public ResponseEntity<MessageResponse> refreshCryptoCurrencies() {
        List<CryptoCurrency> updatedCryptos = cryptoService.fetchAndSaveLatestPrices();
        String message = buildRefreshSuccessMessage(updatedCryptos.size());
        return ResponseEntity.ok(new MessageResponse(message, true));
    }

    private String buildRefreshSuccessMessage(int count) {
        return String.format("Successfully updated %d cryptocurrencies", count);
    }
} 