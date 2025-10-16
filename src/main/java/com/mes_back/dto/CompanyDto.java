package com.mes_back.dto;

import com.mes_back.constant.CompanyType;
import com.mes_back.entity.Company;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CompanyDto {

    private CompanyType companyType;
    private String companyName;
    private String ceoName;
    private String ceoPhone;
    private String businessNum;
    private String zipcode;
    private String addressBase;
    private String addressDetail;
    private String remark;
    private String managerName;
    private String managerPhone;
    private String managerEmail;

    public static CompanyDto fromEntity(Company company) {
        return CompanyDto.builder()
                .companyType(company.getCompanyType())
                .companyName(company.getCompanyName())
                .ceoName(company.getCeoName())
                .ceoPhone(company.getCeoPhone())
                .businessNum(company.getBusinessNum())
                .zipcode(company.getZipcode())
                .addressBase(company.getAddressBase())
                .addressDetail(company.getAddressDetail())
                .remark(company.getRemark())
                .managerName(company.getManagerName())
                .managerPhone(company.getManagerPhone())
                .managerEmail(company.getManagerEmail())
                .build();
    }


}
