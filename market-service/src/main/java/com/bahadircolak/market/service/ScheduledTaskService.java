package com.bahadircolak.market.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduledTaskService {

    private final CryptoService cryptoService;

    /**
     * Update cryptocurrency prices every 10 minutes
     */
    @Scheduled(fixedRate = 600000) // 10 minutes = 600000 milliseconds
    public void updateCryptocurrencyPrices() {
        try {
            log.info("Starting scheduled cryptocurrency price update...");
            var updatedCryptos = cryptoService.fetchAndSaveLatestPrices();
            log.info("Successfully updated {} cryptocurrency prices", updatedCryptos.size());
        } catch (Exception e) {
            log.error("Error during scheduled cryptocurrency price update", e);
        }
    }

    /**
     * Daily market data refresh at midnight
     */
    @Scheduled(cron = "0 0 0 * * *") // Every day at midnight
    public void dailyMarketDataRefresh() {
        try {
            log.info("Starting daily market data refresh...");
            var updatedCryptos = cryptoService.fetchAndSaveLatestPrices();
            log.info("Daily market data refresh completed. Updated {} cryptocurrencies", updatedCryptos.size());
        } catch (Exception e) {
            log.error("Error during daily market data refresh", e);
        }
    }
} 