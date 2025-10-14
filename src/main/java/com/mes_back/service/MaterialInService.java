package com.mes_back.service;

import com.mes_back.dto.MaterialDto;
import com.mes_back.repository.MaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MaterialInService {
    private final MaterialRepository materialRepository;

    public List<MaterialDto> findAll() {
        return materialRepository.findAll().stream()
                .map(material -> MaterialDto.builder()
                        .id(material.getId())
                        .materialName(material.getName())
                        .materialCode(material.getCode())
                        .companyName(material.getCompany().getCompanyName())
                        .spec(material.getSpec())
                        .scale(material.getScale())
                        .manufacturer(material.getManufacturer())
                        .remark(material.getRemark())
                        .build())
                .toList();
    }
}
