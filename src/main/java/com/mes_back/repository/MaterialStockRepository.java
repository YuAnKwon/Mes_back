package com.mes_back.repository;

import com.mes_back.entity.Material;
import com.mes_back.entity.MaterialStock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface MaterialStockRepository extends JpaRepository<MaterialStock,Long> {

    Optional<MaterialStock> findByMaterial(Material material);
}
