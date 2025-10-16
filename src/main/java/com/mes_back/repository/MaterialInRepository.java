package com.mes_back.repository;

import com.mes_back.constant.Yn;
import com.mes_back.entity.Material;
import com.mes_back.entity.MaterialIn;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface MaterialInRepository extends JpaRepository<MaterialIn,Long> {
    List<MaterialIn> findByDelYn(Yn delYn);
    long countByInNumStartingWith(String prefix);
    List<MaterialIn> findByMaterialAndDelYn(Material material, Yn delYn);
    Optional<MaterialIn> findByInNum(String inNum);

}
