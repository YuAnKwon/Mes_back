package com.mes_back.controller;

import com.mes_back.constant.Yn;
import com.mes_back.dto.CompanyDto;
import com.mes_back.dto.CompanyListDto;
import com.mes_back.dto.CompanyStateDto;
import com.mes_back.entity.Company;
import com.mes_back.repository.CompanyRepository;
import com.mes_back.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/master/company")
@RequiredArgsConstructor
public class CampanyController {

    private final CompanyRepository companyRepository;
    private final CompanyService companyService;

    //입력된 데이터를 CompanyDto 객체로 매핑, Service에 있는 등록 로직으로 등록처리(Dto -> Entity -> DB), 등록된 업체 정보를 응답으로 반환
    @PostMapping("/register")
    public ResponseEntity<Company> register(@RequestBody CompanyDto companyDto) {
        Company saved = companyService.registerCompany(companyDto);
        return ResponseEntity.ok(saved);
    }

    //서비스 계층의 findAll() 메서드를 호출해 업체 목록 조회 CompanyListDto 리스트로 반환
    @GetMapping("/list")
    public ResponseEntity<List<CompanyListDto>> getAll() {
        return ResponseEntity.ok(companyService.findAll());
    }

    //업체 거래 상태 변경
    @PatchMapping("/{id}/state")
    public ResponseEntity<Company> updateBusinessYn( @PathVariable Long id, @RequestBody CompanyStateDto companyStateDto ) {
        Yn yn = Yn.valueOf(companyStateDto.getUpdatedState()); // "Y" 또는 "N" 객체
        Company updatedCompany = companyService.updateBusinessYn(id, yn);
        return ResponseEntity.ok(updatedCompany); // ← 반환값 사용

    }

}
