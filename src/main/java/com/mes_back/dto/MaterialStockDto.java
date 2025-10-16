package com.mes_back.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class MaterialStockDto {
    private Long materialId;  //원자재 id
    private String materialCode; // 원자재 번호
    private String materialName; // 원자재명
    private String companyName; // 회사명
    private String manufacturer; // 제조사
    private Integer stock; // 재고
}
