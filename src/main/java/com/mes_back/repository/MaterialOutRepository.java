package com.mes_back.repository;

import com.mes_back.constant.Yn;
import com.mes_back.entity.MaterialOut;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaterialOutRepository extends JpaRepository<MaterialOut, Long>
{
    long countByOutNumStartingWith(String prefix);
    List<MaterialOut> findByDelYn(Yn delYn);
}
