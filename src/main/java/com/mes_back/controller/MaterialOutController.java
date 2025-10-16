package com.mes_back.controller;

import com.mes_back.dto.MaterialInDto;
import com.mes_back.dto.MaterialOutDto;
import com.mes_back.service.MaterialOutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/material")
public class MaterialOutController {
    private final MaterialOutService materialOutService;

    @GetMapping("/out")
    public ResponseEntity<?> findAllActive() {
        return ResponseEntity.ok(materialOutService.findAllOutRegister());
    }

    @PostMapping("/out")
    public ResponseEntity<?> registerOut(@RequestBody List<MaterialOutDto> materialInDtos) {
        try {
            materialOutService.registerOut(materialInDtos);
            return ResponseEntity.ok("출고 등록이 완료되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("출고 등록 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    @GetMapping("/out/list")
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(materialOutService.findAllActive());
    }

    @PutMapping("/out/{id}")
    public ResponseEntity<?> updateRegisterOut(@PathVariable Long id,@RequestBody MaterialOutDto dto){
        try {
            dto.setId(id);
            materialOutService.updateRegisterOut(dto);
            return ResponseEntity.ok("입고 정보가 수정되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body("입고 수정 중 오류 발생: " + e.getMessage());
        }
    }

    // 출고 삭제
    @DeleteMapping("/out/{id}")
    public ResponseEntity<?> softDelete(@PathVariable Long id){
        materialOutService.softDelete(id);
        return ResponseEntity.ok("삭제되었습니다");
    }
}
