package com.bahadircolak.croesus.scheduler;

import com.bahadircolak.croesus.service.CryptoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class CryptoUpdateScheduler {
    
    private static final Logger logger = LoggerFactory.getLogger(CryptoUpdateScheduler.class);
    private final CryptoService cryptoService;
    
    @Autowired
    public CryptoUpdateScheduler(CryptoService cryptoService) {
        this.cryptoService = cryptoService;
    }

    @Scheduled(fixedRate = 300000)
    public void updateCryptoPrices() {
        logger.info("Kripto para fiyatları güncelleniyor...");
        try {
            cryptoService.fetchAndSaveLatestPrices();
            logger.info("Kripto para fiyatları başarıyla güncellendi.");
        } catch (Exception e) {
            logger.error("Kripto para fiyatları güncellenirken hata oluştu: " + e.getMessage(), e);
        }
    }
} 