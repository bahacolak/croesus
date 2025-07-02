package com.bahadircolak.market.service;

import com.bahadircolak.market.constants.MarketConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduledTaskService {

    private final ICryptoService cryptoService;

    @Scheduled(fixedRate = MarketConstants.PRICE_UPDATE_INTERVAL)
    public void updateCryptocurrencyPrices() {
        cryptoService.fetchAndSaveLatestPrices();
    }

    @Scheduled(cron = MarketConstants.DAILY_REFRESH_CRON)
    public void dailyMarketDataRefresh() {
        cryptoService.fetchAndSaveLatestPrices();
    }
} 