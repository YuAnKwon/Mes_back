package com.mes_back.controller;

import com.mes_back.service.MaterialInService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/material")
public class MaterialInController {

    private final MaterialInService materialInService;

    @GetMapping
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(materialInService.findAll());
    }

    @GetMapping("/in")
    public ResponseEntity<?> findAllActive() {
        return ResponseEntity.ok(materialInService.findAllActive());
    }
}
