package com.mes_back.repository;

import com.mes_back.entity.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MaterialRepository extends JpaRepository<Material,Long> {
    Optional<Material> findByCode(String code);
}
