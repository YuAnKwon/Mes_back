package com.mes_back.controller;

import com.mes_back.dto.MaterialInDto;
import com.mes_back.service.MaterialInService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/material")
public class MaterialInController {

    private final MaterialInService materialInService;

    @GetMapping
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(materialInService.findAll());
    }

    //
    @GetMapping("/in")
    public ResponseEntity<?> findAllActive() {
        return ResponseEntity.ok(materialInService.findAllActive());
    }

    //입고 등록
    @PostMapping("/in")
    public ResponseEntity<?> registerIn(@RequestBody List<MaterialInDto> materialInDtos) {
        try {
            materialInService.registerIn(materialInDtos);
            return ResponseEntity.ok("입고 등록이 완료되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("입고 등록 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    //입고수정
    @PutMapping("/in/{id}")
    public ResponseEntity<?> updateRegisterIn(@PathVariable Long id,@RequestBody MaterialInDto dto){
        try {
            dto.setId(id);
            materialInService.updateRegisterIn(dto);
            return ResponseEntity.ok("입고 정보가 수정되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body("입고 수정 중 오류 발생: " + e.getMessage());
        }
    }

    // 입고 삭제
    @DeleteMapping("/in/{id}")
    public ResponseEntity<?> softDelete(@PathVariable Long id){
        materialInService.softDelete(id);
        return ResponseEntity.ok("삭제되었습니다");
    }
}
