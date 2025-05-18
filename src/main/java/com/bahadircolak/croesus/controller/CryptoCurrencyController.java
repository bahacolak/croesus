package com.bahadircolak.croesus.controller;

import com.bahadircolak.croesus.model.CryptoCurrency;
import com.bahadircolak.croesus.service.CryptoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/crypto")
public class CryptoCurrencyController {

    private final CryptoService cryptoService;

    @Autowired
    public CryptoCurrencyController(CryptoService cryptoService) {
        this.cryptoService = cryptoService;
    }

    @GetMapping
    public ResponseEntity<List<CryptoCurrency>> getAllCryptoCurrencies() {
        return ResponseEntity.ok(cryptoService.getAllCryptoCurrencies());
    }

    @PostMapping("/refresh")
    public ResponseEntity<List<CryptoCurrency>> refreshCryptoCurrencies() {
        return ResponseEntity.ok(cryptoService.fetchAndSaveLatestPrices());
    }
} 