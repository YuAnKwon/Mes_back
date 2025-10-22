package com.mes_back.repository;

import com.mes_back.constant.CompanyType;
import com.mes_back.constant.Yn;
import com.mes_back.entity.Company;
import com.mes_back.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    Optional<Company> findByCompanyName(String companyName);

    List<Company> findByCompanyType(CompanyType companyType);

}
