package com.mes_back.service;

import com.mes_back.dto.MaterialStockDto;
import com.mes_back.entity.MaterialIn;
import com.mes_back.entity.MaterialStock;
import com.mes_back.repository.MaterialInRepository;
import com.mes_back.repository.MaterialStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MaterialStockService {
    private final MaterialInRepository materialInRepository;
    private final MaterialStockRepository materialStockRepository;



    public List<MaterialStockDto> getAllStocks() {
        return materialStockRepository.findAll().stream()
                .map(stock -> {
                    var m = stock.getMaterial();
                    return MaterialStockDto.builder()
                            .materialId(m.getId())
                            .materialCode(m.getCode())
                            .materialName(m.getName())
                            .manufacturer(m.getManufacturer())
                            .stock(stock.getStock())

                            .build();
                })
                .toList();
    }
}
