package com.mes_back.entity;

import com.mes_back.constant.Yn;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "routing")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Routing extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String processCode; // 공정코드

    @Column(nullable = false)
    private String processName; // 공정명

    @Column(nullable = false)
    private Integer processTime; // 공정시간(분)

    @Column(columnDefinition = "TEXT")
    private String remark; //비고

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Yn del_yn = Yn.N; //삭제 여부

}
