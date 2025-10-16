package com.mes_back.service;
import com.mes_back.dto.MaterialListDto;
import com.mes_back.repository.MaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
