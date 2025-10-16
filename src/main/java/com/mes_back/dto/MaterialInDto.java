package com.mes_back.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class MaterialInDto {
    private Long id;
    private Integer inAmount;
    private String materialName; // 품목명
    private String materialCode; // 품목번호

    private String companyName; //매입처명


    private String manufacturer; // 제조사
    private String inNum; // 입고번호
    private Integer totalStock; // 총량
    private Date inDate; // 입고일자
    private Date manufactureDate; // 제조일자
    private String delYn;
    private String scale;
    private Integer spec;
    private Integer stock;
    private String specAndScale; // 원자재 규격 + 단위
}
