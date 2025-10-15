package com.mes_back.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class OrderItemInDto {

    private Long id;

    private String lotNum; // lot번호

    private String itemName; // 품목명

    private String itemCode; // 품목코드

    private String company; // 거래처명

    private String type; // 분류

    private String remark; // 비고

    private Integer inAmount; // 수량

    private String inDate; // 입고일자

}
