package com.mes_back.dto;

import com.mes_back.constant.Yn;
import com.mes_back.entity.OrderItem;
import com.mes_back.entity.OrderItemImg;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class OrderItemImgDto {

    private Long id;
    private OrderItem orderItem;
    private String imgOriName;
    private String imgFileName;
    private String imgUrl;
    private Yn repYn = Yn.Y;//대표이미지

    // 엔티티 → DTO 변환
    public static OrderItemImgDto fromEntity(OrderItemImg img) {
        OrderItemImgDto dto = new OrderItemImgDto();
        dto.setId(img.getId());
        dto.setImgOriName(img.getImgOriName());
        dto.setImgFileName(img.getImgFileName());
        dto.setImgUrl("/api" + img.getImgUrl());
        dto.setRepYn(img.getRepYn());
        return dto;
    }
}
