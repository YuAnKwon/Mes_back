package com.mes_back.controller;

import com.mes_back.dto.MaterialListDto;
import com.mes_back.dto.OrderItemListDto;
import com.mes_back.service.MaterialService;
import com.mes_back.service.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/master/orderitem")
@RequiredArgsConstructor
public class OrderItemController {

    final OrderItemService orderItemService;

    //서비스 계층의 findAll() 메서드를 호출해 업체 목록 조회 MaterialListDto 리스트로 반환
    @GetMapping("/list")
    public ResponseEntity<List<OrderItemListDto>> getAll() {
        return ResponseEntity.ok(orderItemService.findAll());
    }
}
