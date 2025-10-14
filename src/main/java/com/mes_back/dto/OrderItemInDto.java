package com.mes_back.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class OrderItemInDto {

    private Long id;
    private String itemName; // 품목명

    private String itemCode; // 품목코드

    private String company; // 거래처명

    private String type; // 분류

    private String remark; // 비고
}
