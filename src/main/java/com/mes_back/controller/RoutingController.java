package com.mes_back.controller;

import com.mes_back.dto.OrderItemInOutDto;
import com.mes_back.dto.RoutingDto;
import com.mes_back.service.RoutingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RoutingController {

    private final RoutingService routingService;

    // 라우팅 목록 조회
    @GetMapping("/routing/list")
    public List<RoutingDto> getRoutingList() {
        return routingService.getRoutingList();
    }
    
    // 라우팅 추가
    @PostMapping("/routing/new")
    public ResponseEntity<?> newRouting(@RequestBody List<RoutingDto> RoutingDto){
        try {
            routingService.newRouting(RoutingDto);
            return ResponseEntity.ok("라우팅 등록 성공");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("등록 중 오류 발생");
        }
    }

    @DeleteMapping("/routing/{id}")
    public ResponseEntity<?> deleteRouting(@PathVariable Long id){
        try {
            routingService.deleteRouting(id);
            return ResponseEntity.ok("라우팅 삭제 성공");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("삭제 중 오류 발생");
        }
    }
    
}
