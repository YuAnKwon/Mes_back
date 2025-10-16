package com.mes_back.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ShipInvoiceDto {
    private Long id; // 수주 입출고 id

    private String companyName; // 거래처명

    private String outNum; // 출고번호

    private String inDate; // 입고일자

    private String outDate; // 출고일자

    private String companyAddr;// 거래처 주소

    private String itemCode; // 품목번호

    private String itemName; // 품목명

    private Integer outAmount; // 출고수량
}
