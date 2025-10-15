package com.mes_back.service;

import com.mes_back.dto.CompanyDto;
import com.mes_back.dto.CompanyListDto;
import com.mes_back.entity.Company;
import com.mes_back.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    //업체 등록(Dto에서 받은 값을 새로 생성한 Company(엔티티) 객체에 넣기 ==> DB에 저장)
    public Company registerCompany(CompanyDto companyDto) {
        Company company = new Company();
        company.setCompanyType(companyDto.getCompanyType());
        company.setCompanyName(companyDto.getCompanyName());
        company.setCeoName(companyDto.getCeoName());
        company.setCeoPhone(companyDto.getCeoPhone());
        company.setBusinessNum(companyDto.getBusinessNum());
        company.setZipcode(companyDto.getZipcode());
        company.setAddressBase(companyDto.getAddressBase());
        company.setAddressDetail(companyDto.getAddressDetail());
        company.setRemark(companyDto.getRemark());
        company.setManagerName(companyDto.getManagerName());
        company.setManagerPhone(companyDto.getManagerPhone());
        company.setManagerEmail(companyDto.getManagerEmail());

        return companyRepository.save(company);
    }

    //업체 조회(Entity -> Dto)
    public List<CompanyListDto> findAll() {
        return companyRepository.findAll().stream()
                .map(CompanyListDto::new)
                .collect(Collectors.toList());
    }


}
