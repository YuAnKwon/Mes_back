package com.mes_back.controller;

import com.mes_back.dto.OrderItemInOutDto;
import com.mes_back.dto.ShipInvoiceDto;
import com.mes_back.entity.OrderItemInout;
import com.mes_back.service.OrderItemInoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderItemInoutController {

    private final OrderItemInoutService orderItemInOutService;

    // 입고등록페이지 list 조회. (사용여부 Y인것만)
    @GetMapping("/orderitem/in/regi/list")
    public List<OrderItemInOutDto> getOrderItemInRegi(){
        return orderItemInOutService.getOrderItemInRegi();
    }

    @PostMapping("/orderitem/in/register")
    public ResponseEntity<?> orderItemInRegister(@RequestBody List<OrderItemInOutDto> orderItemInOutDto){
        try {
            orderItemInOutService.registerIn(orderItemInOutDto);
            return ResponseEntity.ok("입고 등록 성공");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("등록 중 오류 발생");
        }
    }

    // 입고조회페이지 list
    @GetMapping("/orderitem/in/list")
    public List<OrderItemInOutDto> getOrderItemIn(){
        return orderItemInOutService.getOrderItemIn();
    }

    // 입고 수정
    @PutMapping("/orderitem/in/{id}")
    public ResponseEntity<?> updateOrderItemIn(@PathVariable Long id, @RequestBody OrderItemInOutDto dto){
        Long updatedId = orderItemInOutService.updateOrderItemIn(id, dto);
        return ResponseEntity.ok(updatedId);
    }

    // 입고 삭제여부 Y으로 변경
    @DeleteMapping("/orderitem/in/{id}")
    public ResponseEntity<?> deleteOrderItemIn(@PathVariable Long id){
        Long deletedId = orderItemInOutService.deleteOrderItemIn(id);
        return ResponseEntity.ok(deletedId);
    }

    // 출고 등록
    @PostMapping("/orderitem/out/register")
    public ResponseEntity<?> orderItemOutRegister(@RequestBody List<OrderItemInOutDto> orderItemInOutDto){
        try {
            orderItemInOutService.registerOut(orderItemInOutDto);
            return ResponseEntity.ok("출고 등록 성공");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("등록 중 오류 발생");
        }
    }

    // 출고조회페이지 list
    @GetMapping("/orderitem/out/list")
    public List<OrderItemInOutDto> getOrderItemOut(){
        return orderItemInOutService.getOrderItemOut();
    }


    // 출고 수정
    @PutMapping("/orderitem/out/{id}")
    public ResponseEntity<?> updateOrderItemOut(@PathVariable Long id, @RequestBody OrderItemInOutDto dto){
        Long updatedId = orderItemInOutService.updateOrderItemOut(id, dto);
        return ResponseEntity.ok(updatedId);
    }

    // 출고 삭제여부 Y으로 변경
    @DeleteMapping("/orderitem/out/{id}")
    public ResponseEntity<?> deleteOrderItemOut(@PathVariable Long id){
        Long deletedId = orderItemInOutService.deleteOrderItemOut(id);
        return ResponseEntity.ok(deletedId);
    }

    // 출하증
    @GetMapping("/orderitem/ship/{id}")
    public ShipInvoiceDto getShipmentInvoice(@PathVariable Long id){
        return orderItemInOutService.getShipmentInvoice(id);
    }

}
