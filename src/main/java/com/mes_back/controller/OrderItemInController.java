package com.mes_back.controller;

import com.mes_back.dto.OrderItemInDto;
import com.mes_back.service.OrderItemInService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping("/orderitem/in/register")
    public ResponseEntity<?> orderItemInRegister(@RequestBody List<OrderItemInDto> orderItemInDto){
        try {
            orderItemInService.registerIn(orderItemInDto);
            return ResponseEntity.ok("입고 등록 성공");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("등록 중 오류 발생");
        }
    }

    // 입고조회페이지 list
    @GetMapping("/orderitem/in/list")
    public List<OrderItemInDto> getOrderItemIn(){
        return orderItemInService.getOrderItemIn();
    }

}
