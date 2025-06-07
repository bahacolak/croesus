package com.bahadircolak.market.controller;

import com.bahadircolak.market.dto.response.MessageResponse;
import com.bahadircolak.market.model.CryptoCurrency;
import com.bahadircolak.market.service.CryptoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/crypto")
@RequiredArgsConstructor
@Slf4j
public class CryptoController {

    private final CryptoService cryptoService;

    @GetMapping
    public ResponseEntity<List<CryptoCurrency>> getAllCryptoCurrencies() {
        try {
            List<CryptoCurrency> cryptos = cryptoService.getAllCryptoCurrencies();
            return ResponseEntity.ok(cryptos);
        } catch (Exception e) {
            log.error("Error fetching all cryptocurrencies", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{symbol}")
    public ResponseEntity<CryptoCurrency> getCryptoCurrencyBySymbol(@PathVariable String symbol) {
        try {
            Optional<CryptoCurrency> crypto = cryptoService.getCryptoCurrencyBySymbol(symbol);
            if (crypto.isPresent()) {
                return ResponseEntity.ok(crypto.get());
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error fetching cryptocurrency by symbol: {}", symbol, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{symbol}/price")
    public ResponseEntity<BigDecimal> getCryptoPriceBySymbol(@PathVariable String symbol) {
        try {
            BigDecimal price = cryptoService.getCryptoPriceBySymbol(symbol);
            return ResponseEntity.ok(price);
        } catch (RuntimeException e) {
            log.error("Error fetching price for symbol: {}", symbol, e);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Unexpected error fetching price for symbol: {}", symbol, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<CryptoCurrency>> searchCryptoCurrencies(@RequestParam String q) {
        try {
            List<CryptoCurrency> cryptos = cryptoService.searchCryptoCurrencies(q);
            return ResponseEntity.ok(cryptos);
        } catch (Exception e) {
            log.error("Error searching cryptocurrencies with query: {}", q, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/gainers")
    public ResponseEntity<List<CryptoCurrency>> getTopGainers() {
        try {
            List<CryptoCurrency> gainers = cryptoService.getTopGainers();
            return ResponseEntity.ok(gainers);
        } catch (Exception e) {
            log.error("Error fetching top gainers", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/losers")
    public ResponseEntity<List<CryptoCurrency>> getTopLosers() {
        try {
            List<CryptoCurrency> losers = cryptoService.getTopLosers();
            return ResponseEntity.ok(losers);
        } catch (Exception e) {
            log.error("Error fetching top losers", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<MessageResponse> refreshCryptoCurrencies() {
        try {
            List<CryptoCurrency> updatedCryptos = cryptoService.fetchAndSaveLatestPrices();
            String message = String.format("Successfully updated %d cryptocurrencies", updatedCryptos.size());
            return ResponseEntity.ok(new MessageResponse(message));
        } catch (Exception e) {
            log.error("Error refreshing cryptocurrency data", e);
            return ResponseEntity.internalServerError()
                    .body(new MessageResponse("Failed to refresh cryptocurrency data: " + e.getMessage(), false));
        }
    }
} 