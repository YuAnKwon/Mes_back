package com.mes_back.dto;

import com.mes_back.constant.CompanyType;
import com.mes_back.constant.EnumKoreanMapper;
import com.mes_back.constant.Yn;
import com.mes_back.entity.Company;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CompanyListDto {

    private Long id;
    private String companyType;
    private String companyName;
    private String ceoName;
    private String address;
    private String businessYn = "Y";
    private String remark;

    //DB에서 가져온 값을 Dto에 복사
    public CompanyListDto(Company company) {
        this.id = company.getId();
        this.companyType = EnumKoreanMapper.getCompanyTypeKorean(company.getCompanyType().name());
        this.companyName = company.getCompanyName();
        this.ceoName = company.getCeoName();
        this.address = company.getAddressBase() + " " + company.getAddressDetail();
        this.businessYn = EnumKoreanMapper.getBusinessYnKorean(company.getBusinessYn().name());
        this.remark = company.getRemark();
    }
}
