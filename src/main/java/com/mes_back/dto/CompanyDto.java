package com.mes_back.dto;

import com.mes_back.constant.CompanyType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CompanyDto {

    private Long id;
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

}
