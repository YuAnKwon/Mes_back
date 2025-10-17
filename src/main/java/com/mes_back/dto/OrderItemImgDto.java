package com.mes_back.dto;

import com.mes_back.constant.Yn;
import com.mes_back.entity.OrderItem;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class OrderItemImgDto {

    private OrderItem orderItem;
    private String imgOriName;
    private String imgFileName;
    private String imgUrl;
    private Yn repYn = Yn.Y;//대표이미지
}
