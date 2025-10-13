package com.mes_back.entity;

import com.mes_back.constant.Yn;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "Material_out")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaterialOut extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "material_out_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material_id")
    private Material material;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material_in_id")
    private MaterialIn materialIn; // 어떤 입고건에서 출고되었는지

    @Column(name = "out_amount")
    private Integer outAmount;

    @Column(name = "out_num", unique = true)
    private String outNum;

    @Column(name = "out_date")
    private LocalDate outDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "del_yn", length = 1)
    private Yn delYn = Yn.N;

}
