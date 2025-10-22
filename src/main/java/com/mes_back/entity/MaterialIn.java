package com.mes_back.entity;


import com.mes_back.constant.Yn;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "Material_in")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaterialIn extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "material_in_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material_id")
    private Material material; // 원자재 품목 (별도 테이블 존재 가정)

    @Column(name = "manufacture_date", nullable = false)
    private Date manufactureDate;

    @Column(name = "in_amount", nullable = false)
    private Integer inAmount;

    @Column(name = "in_num")
    private String inNum;

    @Column(name = "in_date", nullable = false, length = 0)
    private Date inDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "del_yn", nullable = false)
    private Yn delYn = Yn.N;

    private Integer stock;
}