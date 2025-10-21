package com.mes_back.dto;


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
}