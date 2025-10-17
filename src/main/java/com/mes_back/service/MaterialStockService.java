package com.mes_back.service;

import com.mes_back.dto.MaterialStockDto;
import com.mes_back.entity.Material;
import com.mes_back.entity.MaterialIn;
import com.mes_back.entity.MaterialStock;
import com.mes_back.repository.MaterialInRepository;
import com.mes_back.repository.MaterialRepository;
import com.mes_back.repository.MaterialStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MaterialStockService {
    private final MaterialRepository materialRepository;
    private final MaterialStockRepository materialStockRepository;



    public List<MaterialStockDto> getAllStocks() {
        // 1️⃣ 모든 원자재 조회
        List<Material> materials = materialRepository.findAll();

        // 2️⃣ 기존 재고 테이블 조회 (Map으로 변환)
        Map<Long, Integer> stockMap = materialStockRepository.findAll().stream()
                .collect(Collectors.toMap(
                        stock -> stock.getMaterial().getId(),
                        MaterialStock::getStock
                ));

        // 3️⃣ 모든 원자재를 순회하면서 stock이 없으면 0으로 설정
        return materials.stream()
                .map(m -> MaterialStockDto.builder()
                        .materialId(m.getId())
                        .materialCode(m.getCode())
                        .materialName(m.getName())
                        .manufacturer(m.getManufacturer())
                        .companyName(m.getCompany().getCompanyName())
                        .stock(stockMap.getOrDefault(m.getId(), 0)) // 없으면 0
                        .build())
                .toList();
    }
}
