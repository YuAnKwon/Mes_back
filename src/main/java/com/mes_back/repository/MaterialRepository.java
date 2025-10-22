package com.mes_back.repository;

import com.mes_back.constant.Yn;
import com.mes_back.entity.Company;
import com.mes_back.entity.Material;
import com.mes_back.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface MaterialRepository extends JpaRepository<Material,Long> {
    Optional<Material> findByCode(String code);

    List<Material> findByUseYn(Yn useYn);

    List<Material> findByCompany_BusinessYn(Yn businessYn);

}
