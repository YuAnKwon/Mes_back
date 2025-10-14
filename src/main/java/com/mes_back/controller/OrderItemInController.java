package com.mes_back.controller;

import com.mes_back.dto.OrderItemInDto;
import com.mes_back.service.OrderItemInService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderItemInController {

    private final OrderItemInService orderItemInService;

    // 입고등록페이지 list 조회. (사용여부 Y인것만)
    @GetMapping("/orderitem/in/regi/list")
    public List<OrderItemInDto> getOrderItemInRegi(){
        return orderItemInService.getOrderItemInRegi();
    }

}
