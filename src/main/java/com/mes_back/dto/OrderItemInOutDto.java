package com.mes_back.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class OrderItemInOutDto {

    private Long id;

    private String lotNum; // lot번호

    private String outNum; //출고번호

    private String itemName; // 품목명

    private String itemCode; // 품목코드

    private String company; // 거래처명

    private String type; // 분류

    private String remark; // 비고

    private Integer inAmount; // 입고수량

    private String inDate; // 입고일자

    private Integer outAmount; // 출고수량

    private String outDate; // 출고일자

    private String isProcessCompleted; //공정 진행 완료 여부

}
