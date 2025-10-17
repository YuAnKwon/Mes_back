package com.mes_back.dto;

import com.mes_back.constant.EnumKoreanMapper;
import com.mes_back.entity.Company;
import com.mes_back.entity.OrderItem;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class OrderItemListDto {
    private Long id;
    private String itemCode; // 품목코드
    private String itemName; // 품목명
    private Company company; // 매입처명
    private String type; // 분류 (ENUM)
    private String color; // 색상
    private String useYn = "Y"; // 사용여부
    private String remark; // 비고

    //DB에서 가져온 값을 Dto에 복사
    public OrderItemListDto(OrderItem orderItem) {
        this.id =  orderItem.getId();
        this.itemCode = orderItem.getItemCode();
        this.itemName = orderItem.getItemName();
        this.company = orderItem.getCompany();
        this.type =  orderItem.getType().name();
        this.color = orderItem.getColor();
        this.useYn = EnumKoreanMapper.getBusinessYnKorean(orderItem.getUseYn().name());
        this.remark = orderItem.getRemark();

    }
}
