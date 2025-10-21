package com.mes_back.dto;


import com.mes_back.entity.OrderItemRouting;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class OrderItemRoutingDto {
    private Long routingId; // Routing PK
    private Integer routingOrder;

    public static OrderItemRoutingDto fromEntity(OrderItemRouting entity) {
        return OrderItemRoutingDto.builder()
                .routingId(entity.getRouting().getId())
                .routingOrder(entity.getRoutingOrder())
                .build();
    }
}