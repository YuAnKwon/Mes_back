package com.mes_back.controller;

import com.mes_back.service.MaterialOutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/material")
public class MaterialOutController {
    private final MaterialOutService materialOutService;

    @GetMapping("/out")
    public ResponseEntity<?> findAllForOutRegister() {
        return ResponseEntity.ok(materialOutService.findAllOutRegister());
    }
}
