package com.mes_back.service;
import com.mes_back.constant.Yn;
import com.mes_back.dto.MaterialListDto;
import com.mes_back.entity.Company;
import com.mes_back.entity.Material;
import com.mes_back.repository.MaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MaterialService {

    private final MaterialRepository materialRepository;

    //원자재 조회(Entity -> Dto)
    public List<MaterialListDto> findAll() {
        return materialRepository.findAll().stream()
                .map(MaterialListDto::new)
                .collect(Collectors.toList());
    }

    //원자재 거래상태 변경
    @Transactional
    public Long updateBusinessYn(Long id, Yn updatedYn) {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 업체가 존재하지 않습니다."));
        if( updatedYn == Yn.Y ) {
            material.setUseYn(Yn.N);
        }else {
            material.setUseYn(Yn.Y);
        }
        materialRepository.save(material);
        return material.getId();
    }
}
