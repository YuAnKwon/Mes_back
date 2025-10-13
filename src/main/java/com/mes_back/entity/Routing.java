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
    private String processCode;

    @Column(nullable = false)
    private String processName;

    @Column(nullable = false)
    private Integer processTime;

    @Column(columnDefinition = "TEXT")
    private String remark;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Yn del_yn = Yn.N;

}
