package com.mes_back.entity;

import com.mes_back.constant.CompanyType;
import com.mes_back.constant.Yn;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "company")
@Getter
@Setter
@ToString
public class Company extends BaseTimeEntity {
    @Id
    @Column(name = "company_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CompanyType company_type;

    @Column(nullable = false)
    private String company_name;

    @Column(nullable = false)
    private String ceo_name;

    @Column(nullable = false)
    private String ceo_phone;
    private String business_num;
    private String zipcode;
    private String address_base;
    private String address_detail;
    private String remark;
    private String manager_name;
    private String manager_phone;
    private String manager_email;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Yn business_yn;

}
