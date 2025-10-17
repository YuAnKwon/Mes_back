package com.mes_back.dto;

import com.mes_back.constant.CoatingMethod;
import com.mes_back.constant.OrderItemType;
import com.mes_back.entity.Company;
import com.mes_back.entity.Material;
import com.mes_back.entity.OrderItem;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class OrderItemDto {

    private Long id;
    private String company;// 거래처명
    private String itemCode; // 품목코드
    private String itemName; // 품목명
    private String type; // 분류
    private String coatingMethod; // 도장방식
    private Integer unitPrice; // 품목단가
    private String color; // 색상
    private String remark; // 비고

    private List<OrderItemImgDto> images;

    public static OrderItemDto fromEntity(OrderItem orderItem) {

        List<OrderItemImgDto> images = orderItem.getImages().stream()
                .map(OrderItemImgDto::fromEntity)
                .collect(Collectors.toList());


        return OrderItemDto.builder()
                .id(orderItem.getId())
                .company(orderItem.getCompany().getCompanyName())
                .itemCode(orderItem.getItemCode())
                .itemName(orderItem.getItemName())
                .type(orderItem.getType().name())
                .coatingMethod(orderItem.getCoatingMethod().name())
                .unitPrice(orderItem.getUnitPrice())
                .color(orderItem.getColor())
                .remark(orderItem.getRemark())
                .images(images) // 👈 여기 포함
                .build();
    }


}
