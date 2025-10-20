package com.mes_back.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkOrderDto {
    private OrderItemDto orderItem;
    private List<RoutingDto> routingList;
}
