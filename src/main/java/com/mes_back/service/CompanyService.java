package com.mes_back.service;

import com.mes_back.constant.Yn;
import com.mes_back.dto.CompanyDto;
import com.mes_back.dto.CompanyListDto;
import com.mes_back.entity.Company;
import com.mes_back.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    //register and update
    //업체 등록(Dto에서 받은 값을 새로 생성한 Company(엔티티) 객체에 넣기 ==> DB에 저장)
    @Transactional
    public Company saveCompany(Long id, CompanyDto dto) {
        Company company;

        if (id == null) {
            // 신규 등록
            company = new Company();
        } else {
            // 수정
            company = companyRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("해당 업체가 존재하지 않습니다."));
        }

        // 공통 필드 설정
        company.setCompanyType(dto.getCompanyType());
        company.setCompanyName(dto.getCompanyName());
        company.setCeoName(dto.getCeoName());
        company.setCeoPhone(dto.getCeoPhone());
        company.setBusinessNum(dto.getBusinessNum());
        company.setZipcode(dto.getZipcode());
        company.setAddressBase(dto.getAddressBase());
        company.setAddressDetail(dto.getAddressDetail());
        company.setRemark(dto.getRemark());
        company.setManagerName(dto.getManagerName());
        company.setManagerPhone(dto.getManagerPhone());
        company.setManagerEmail(dto.getManagerEmail());

        return companyRepository.save(company);
    }

    //업체 조회(Entity -> Dto)
    public List<CompanyListDto> findAll() {
        return companyRepository.findAll().stream()
                .map(CompanyListDto::new)
                .collect(Collectors.toList());
    }

    //업체 거래상태 변경
    public Company updateBusinessYn(Long id, Yn updatedYn) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 업체가 존재하지 않습니다."));
        company.setBusinessYn(updatedYn);
        return companyRepository.save(company);
    }

    //업체 상세페이지 조회
    public CompanyDto getCompanyDetail(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 업체가 존재하지 않습니다."));
        return CompanyDto.fromEntity(company);
    }




}
