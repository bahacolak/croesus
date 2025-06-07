package com.bahadircolak.portfolio.repository;

import com.bahadircolak.portfolio.model.Asset;
import com.bahadircolak.portfolio.model.Asset.AssetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {
    
    Optional<Asset> findBySymbol(String symbol);
    
    List<Asset> findByType(AssetType type);
    
    Boolean existsBySymbol(String symbol);
} 