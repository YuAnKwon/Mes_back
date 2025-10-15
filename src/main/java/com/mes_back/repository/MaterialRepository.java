package com.mes_back.repository;

import com.mes_back.entity.Material;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MaterialRepository extends JpaRepository<Material,Long> {
    Optional<Material> findByCode(String code);
}
