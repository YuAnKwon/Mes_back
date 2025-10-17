package com.mes_back.controller;

import com.mes_back.dto.MaterialListDto;
import com.mes_back.dto.MaterialStockDto;
import com.mes_back.service.MaterialStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/material")
public class StockController {
    private final MaterialStockService materialStockService;

    @GetMapping("/totalstock")
    public ResponseEntity<List<MaterialStockDto>> getAllStocks() {
        return ResponseEntity.ok(materialStockService.getAllStocks());
    }
}
