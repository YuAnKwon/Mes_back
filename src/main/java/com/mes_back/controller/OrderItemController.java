package com.mes_back.controller;

import com.mes_back.constant.Yn;
import com.mes_back.dto.CompanyStateDto;
import com.mes_back.dto.MaterialListDto;
import com.mes_back.dto.OrderItemListDto;
import com.mes_back.service.MaterialService;
import com.mes_back.service.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/master/orderitem")
@RequiredArgsConstructor
public class OrderItemController {

    final OrderItemService orderItemService;



    //서비스 계층의 findAll() 메서드를 호출해 업체 목록 조회 MaterialListDto 리스트로 반환
    @GetMapping("/list")
    public ResponseEntity<List<OrderItemListDto>> getCompany_BusinessYnList() {
        return ResponseEntity.ok(orderItemService.findByCompany_BusinessYn(Yn.Y));
    }

    //수주 거래 상태 변경
    @PatchMapping("/{id}/state")
    public ResponseEntity<Long> updateBusinessYn(@PathVariable Long id, @RequestBody CompanyStateDto companyStateDto ) {
        Yn yn = Yn.valueOf(companyStateDto.getUpdatedState()); // "Y" 또는 "N" 객체
        Long updatedId = orderItemService.updateUseYn(id, yn);
        return ResponseEntity.ok(updatedId); // ← 반환값 사용

    }
}
