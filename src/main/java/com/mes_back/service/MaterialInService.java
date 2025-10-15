package com.mes_back.service;

import com.mes_back.constant.Yn;
import com.mes_back.dto.MaterialDto;
import com.mes_back.dto.MaterialInDto;
import com.mes_back.dto.OrderItemInDto;
import com.mes_back.entity.Material;
import com.mes_back.entity.MaterialIn;
import com.mes_back.entity.OrderItem;
import com.mes_back.entity.OrderItemInout;
import com.mes_back.repository.MaterialInRepository;
import com.mes_back.repository.MaterialRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class MaterialInService {
    private final MaterialRepository materialRepository;
    private final MaterialInRepository materialInRepository;

    public List<MaterialDto> findAll() {
        return materialRepository.findAll().stream()
                .map(material -> MaterialDto.builder()
                        .id(material.getId())
                        .materialName(material.getName())
                        .materialCode(material.getCode())
                        .companyName(material.getCompany().getCompanyName())
                        .spec(material.getSpec())
                        .scale(material.getScale())
                        .specAndScale(material.getSpec()+material.getScale())
                        .manufacturer(material.getManufacturer())

                        .remark(material.getRemark())
                        .build())
                .toList();
    }

    // 전체 조회 (삭제 안된것들)
    public List<MaterialInDto> findAllActive() {
        return materialInRepository.findByDelYn(Yn.N).stream()
                .map(in -> {
                    Material m = in.getMaterial();
                    if (m == null) return null; // 혹시 null일 경우 방어

                    Integer spec = m.getSpec() != null ? m.getSpec() : 0;
                    String scale = m.getScale() != null ? m.getScale() : "";

                    int totalStock = (in.getInAmount() != null ? in.getInAmount() : 0) * spec;

                    return MaterialInDto.builder()
                            .id(in.getId())
                            .materialName(m.getName())
                            .materialCode(m.getCode())
                            .companyName(m.getCompany() != null ? m.getCompany().getCompanyName() : "")
                            .inAmount(in.getInAmount())
                            .spec(spec)
                            .scale(scale)
                            .specAndScale(spec + scale)
                            .manufacturer(m.getManufacturer())
                            .inNum(in.getInNum())
                            .inDate(in.getInDate())
                            .manufactureDate(in.getManufactureDate())
                            .delYn(in.getDelYn() != null ? in.getDelYn().name() : "N")
                            .stock(totalStock)
                            .build();
                })
                .filter(Objects::nonNull) // null 방어 후 필터링
                .toList();
    }


    //삭제
    public void softDelete(Long id) {
        MaterialIn materialIn = materialInRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("입고 데이터를 찾을 수 없습니다."));
        materialIn.setDelYn(Yn.Y);
        materialInRepository.save(materialIn);
    }

    public void registerIn(List<MaterialInDto> materialInDtos) {

        // 날짜별 LOT 번호 순번 관리용 Map
        Map<LocalDate, Long> lotCountMap = new HashMap<>();

        for (MaterialInDto dto : materialInDtos) {
            // 1️⃣ 원자재 조회
            Material material = materialRepository.findByCode(dto.getMaterialCode())
                    .orElseThrow(() -> new EntityNotFoundException("품목코드에 해당하는 원자재를 찾을 수 없습니다: " + dto.getMaterialCode()));

            // 2️⃣ 날짜 변환 (java.util.Date → LocalDate)
            LocalDate inDate = dto.getInDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            // 3️⃣ LOT 번호 카운트 확인
            long count;
            if (lotCountMap.containsKey(inDate)) {
                count = lotCountMap.get(inDate);
            } else {
                count = materialInRepository.countByInNumStartingWith(
                        "LOT-" + inDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                );
            }

            // 4️⃣ LOT 번호 생성
            String lotNum = String.format("LOT-%s-%03d",
                    inDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")),
                    count + 1);

            // Map 순번 갱신
            lotCountMap.put(inDate, count + 1);

            // 5️⃣ Entity 생성
            MaterialIn materialIn = MaterialIn.builder()
                    .material(material)
                    .inAmount(dto.getInAmount())
                    .manufactureDate(dto.getManufactureDate())
                    .inDate(dto.getInDate())
                    .inNum(lotNum)
                    .delYn(Yn.N)
                    .build();

            // 6️⃣ 저장
            materialInRepository.save(materialIn);
        }
    }

    public void updateRegisterIn(MaterialInDto dto) {
        MaterialIn materialIn = materialInRepository.findById(dto.getId())
                .orElseThrow(() -> new EntityNotFoundException("입고를 찾을 수 없습니다."));

        if (dto.getInAmount() != null) materialIn.setInAmount(dto.getInAmount()); // 입고수량
        if (dto.getInDate() != null) materialIn.setInDate(dto.getInDate()); // 입고 일자
        if (dto.getManufactureDate() != null) materialIn.setManufactureDate(dto.getManufactureDate()); // 제조 일자


    }


}
