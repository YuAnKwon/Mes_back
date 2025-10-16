package com.mes_back.repository;

import com.mes_back.entity.MaterialOut;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterialOutRepository extends JpaRepository<MaterialOut, Integer>
{
    long countByOutNumStartingWith(String prefix);
}
