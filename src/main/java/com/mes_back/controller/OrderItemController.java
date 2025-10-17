package com.mes_back.controller;

import com.mes_back.constant.Yn;
import com.mes_back.dto.*;
import com.mes_back.entity.Material;
import com.mes_back.entity.OrderItem;
import com.mes_back.service.MaterialService;
import com.mes_back.service.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/master/orderitem")
@RequiredArgsConstructor
public class OrderItemController {

    final OrderItemService orderItemService;


    //입력된 데이터를 OrderItemDto 객체로 매핑, Service에 있는 등록 로직으로 등록처리(Dto -> Entity -> DB), 등록된 업체 정보를 응답으로 반환
    @PostMapping("/register")
    public ResponseEntity<OrderItem> register(
            @RequestPart("data") OrderItemDto orderItemDto,
            @RequestPart(value = "imgUrl", required = false) List<MultipartFile> imgFiles) {

        OrderItem saved = orderItemService.saveOrderItem(null, orderItemDto, imgFiles);

        System.out.println("imgFiles: " + (imgFiles == null ? "null" : imgFiles.size() + "개"));

        return ResponseEntity.ok(saved);
    }

    //서비스 계층의 findAll() 메서드를 호출해 업체 목록 조회 OrderItemListDto 리스트로 반환
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

    //수주대상품목 상세 조회
    @GetMapping("/detail/{id}")
    public ResponseEntity<OrderItemDto> getDetail(@PathVariable Long id) {
        return ResponseEntity.ok(orderItemService.getOrderItemDetail(id));
    }


    //수주대상품목 수정
    @PatchMapping("/detail/{id}")
    public ResponseEntity<String> updateDetail(
            @PathVariable Long id,
            @RequestPart("data") OrderItemDto dto,
            @RequestPart(value = "imgUrl", required = false) List<MultipartFile> imgFiles
    ) {
        orderItemService.saveOrderItem(id, dto, imgFiles);
        return ResponseEntity.ok("수주대상품목 정보 수정 완료");
    }

}
