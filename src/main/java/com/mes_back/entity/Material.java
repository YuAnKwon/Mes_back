package com.mes_back.entity;

import com.mes_back.constant.MaterialType;
import com.mes_back.constant.Yn;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "material")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Material extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "material_id")
    private Long id;  // 원자재 id (PK, auto_increment)

    @Column(name = "material_name", nullable = false)
    private String name;  // 품목명

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;  // 업체명 (FK)

    @Column(name = "material_code")
    private String code;  // 품목번호

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private MaterialType type;  // 분류 (ENUM)

    @Column(name = "color")
    private String color;  // 색상

    @Column(name = "spec")
    private Integer spec;  // 규격

    @Column(name = "scale")
    private String scale;  // 규격 단위

    @Column(name = "manufacturer")
    private String manufacturer;  // 제조사

    @Column(name = "remark", columnDefinition = "TEXT")
    private String remark;  // 비고

    @Enumerated(EnumType.STRING)
    @Column(name = "use_yn", nullable = false)
    private Yn useYn;  // 사용여부 (Y/N)
}
