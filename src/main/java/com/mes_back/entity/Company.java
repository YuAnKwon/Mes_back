package com.mes_back.entity;

import com.mes_back.constant.CompanyType;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "company")
@Getter
@Setter
@ToString
public class Company {
    private Long company_id;
    private CompanyType company_type;
    private String company_name;
    private String ceo_name;
    private String ceo_phone;
    private String business_num;
    private String zipcode;
    private String address_base;
    private String address_detail;
    private String remark;
    private String manager_name;
    private String manager_phone;
    private String manager_email;
    private Yn business_yn;

}
