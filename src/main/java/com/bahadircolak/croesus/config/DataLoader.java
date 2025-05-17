package com.bahadircolak.croesus.config;

import com.bahadircolak.croesus.model.Asset;
import com.bahadircolak.croesus.model.Asset.AssetType;
import com.bahadircolak.croesus.model.Role;
import com.bahadircolak.croesus.model.Role.ERole;
import com.bahadircolak.croesus.repository.AssetRepository;
import com.bahadircolak.croesus.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final AssetRepository assetRepository;

    @Override
    public void run(String... args) {
        loadRoles();
        loadAssets();
    }

    private void loadRoles() {
        if (roleRepository.count() == 0) {
            log.info("Loading roles...");
            
            Role userRole = new Role();
            userRole.setName(ERole.ROLE_USER);
            
            Role adminRole = new Role();
            adminRole.setName(ERole.ROLE_ADMIN);
            
            roleRepository.saveAll(Arrays.asList(userRole, adminRole));
            
            log.info("Roles loaded successfully.");
        }
    }

    private void loadAssets() {
        if (assetRepository.count() == 0) {
            log.info("Loading assets...");
            
            Asset btc = new Asset(null, "BTC", "Bitcoin", AssetType.CRYPTO,
                    new BigDecimal("60000.00"), new BigDecimal("1200.50"), new BigDecimal("2.05"));
            
            Asset eth = new Asset(null, "ETH", "Ethereum", AssetType.CRYPTO, 
                    new BigDecimal("3500.00"), new BigDecimal("85.75"), new BigDecimal("2.45"));
            
            Asset sol = new Asset(null, "SOL", "Solana", AssetType.CRYPTO, 
                    new BigDecimal("140.00"), new BigDecimal("8.25"), new BigDecimal("6.25"));
            
            Asset usd = new Asset(null, "USD", "US Dollar", AssetType.CURRENCY,
                    new BigDecimal("32.50"), new BigDecimal("-0.15"), new BigDecimal("-0.45"));
            
            Asset eur = new Asset(null, "EUR", "Euro", AssetType.CURRENCY, 
                    new BigDecimal("35.30"), new BigDecimal("0.25"), new BigDecimal("0.70"));
            
            Asset gold = new Asset(null, "XAU", "Gold", AssetType.PRECIOUS_METAL,
                    new BigDecimal("2200.00"), new BigDecimal("15.50"), new BigDecimal("0.70"));
            
            Asset silver = new Asset(null, "XAG", "Silver", AssetType.PRECIOUS_METAL, 
                    new BigDecimal("28.50"), new BigDecimal("0.60"), new BigDecimal("2.15"));
            
            assetRepository.saveAll(Arrays.asList(btc, eth, sol, usd, eur, gold, silver));
            
            log.info("Assets loaded successfully.");
        }
    }
} 