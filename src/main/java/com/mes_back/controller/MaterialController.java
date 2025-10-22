package com.mes_back.controller;

import com.mes_back.constant.Yn;
import com.mes_back.dto.CompanyDto;
import com.mes_back.dto.CompanyStateDto;
import com.mes_back.dto.MaterialDto;
import com.mes_back.dto.MaterialListDto;
import com.mes_back.entity.Company;
import com.mes_back.entity.Material;
import com.mes_back.service.MaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/master/material")
@RequiredArgsConstructor
public class MaterialController {

    private final MaterialService materialService;

    //입력된 데이터를 MaterialDto 객체로 매핑, Service에 있는 등록 로직으로 등록처리(Dto -> Entity -> DB), 등록된 업체 정보를 응답으로 반환
    @PostMapping("/register")
    public ResponseEntity<Material> register(@RequestBody MaterialDto materialDto) {
        System.out.println("DTO company: " + materialDto.getCompanyName());
        Material saved = materialService.saveMaterial(null, materialDto);
        return ResponseEntity.ok(saved);
    }

    //서비스 계층의 findAll() 메서드를 호출해 업체 목록 조회 MaterialListDto 리스트로 반환
    @GetMapping("/list")
    public ResponseEntity<List<MaterialListDto>> getFindByCompany_BusinessYn() {
        return ResponseEntity.ok(materialService.findByCompany_BusinessYn());
    }

    //원자재 거래 상태 변경
    @PatchMapping("/{id}/state")
    public ResponseEntity<Long> updateBusinessYn(@PathVariable Long id, @RequestBody CompanyStateDto companyStateDto ) {
        Yn yn = Yn.valueOf(companyStateDto.getUpdatedState()); // "Y" 또는 "N" 객체
        Long updatedId = materialService.updateBusinessYn(id, yn);
        return ResponseEntity.ok(updatedId); // ← 반환값 사용

    }

    //업체 상세 조회
    @GetMapping("/detail/{id}")
    public ResponseEntity<MaterialDto> getDetail(@PathVariable Long id) {
        return ResponseEntity.ok(materialService.getMaterialDetail(id));
    }


    //업체 수정
    @PutMapping("/detail/{id}")
    public ResponseEntity<String> updateDetail(
            @PathVariable Long id,
            @RequestBody MaterialDto dto
    ) {
        materialService.saveMaterial(id, dto);
        return ResponseEntity.ok("업체 정보 수정 완료");
    }

}
