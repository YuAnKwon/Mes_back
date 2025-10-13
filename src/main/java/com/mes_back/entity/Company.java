package com.mes_back.entity;

import com.mes_back.constant.CompanyType;
import com.mes_back.constant.Yn;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "company")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company extends BaseTimeEntity {
    @Id
    @Column(name = "company_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CompanyType companyType;

    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false)
    private String ceoName;

    @Column(nullable = false)
    private String ceoPhone;

    @Column(nullable = false)
    private String businessNum;

    @Column(nullable = false)
    private String zipcode;

    @Column(nullable = false)
    private String addressBase;

    @Column(nullable = false)
    private String addressDetail;

    private String remark;
    private String managerName;
    private String managerPhone;
    private String managerEmail;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Yn businessYn = Yn.Y;

}
