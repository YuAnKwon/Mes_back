package com.mes_back.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class MaterialDto {
    private Long id;
    private String materialName; // 품목명
    private String materialCode; // 품목번호
    private String companyName; // 매입처명
    private Integer spec; // 규격
    private String scale; // 단위
    private String manufacturer; // 제조사
    private String remark; // 비고
}
