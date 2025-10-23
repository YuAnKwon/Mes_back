package com.mes_back.service;

import com.mes_back.constant.Yn;
import com.mes_back.dto.MaterialDto;
import com.mes_back.dto.MaterialInDto;
import com.mes_back.entity.Material;
import com.mes_back.entity.MaterialIn;

import com.mes_back.entity.MaterialStock;
import com.mes_back.repository.MaterialInRepository;
import com.mes_back.repository.MaterialRepository;
import com.mes_back.repository.MaterialStockRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final MaterialStockRepository materialStockRepository;

    public List<MaterialDto> findAll() {
        return materialRepository.findByUseYn(Yn.Y).stream()
                .map(material -> MaterialDto.builder()
                        .id(material.getId())
                        .materialName(material.getName())
                        .materialCode(material.getCode())
                        .companyName(material.getCompany().getCompanyName())
                        .spec(material.getSpec())
                        .scale(material.getScale())
                        .specAndScale(material.getSpec()+material.getScale())
                        .manufacturer(material.getManufacturer())
                        .useYn(material.getUseYn().name())
                        .remark(material.getRemark())
                        .build())
                .toList();
    }

    // 전체 조회 (삭제 안된것들)
    public List<MaterialInDto> findAllActive() {
        return materialInRepository.findByDelYn(Yn.N).stream()
                // 🔸 재고가 0보다 큰 경우만 표시
                .filter(in -> in.getStock() != null && in.getStock() > 0)
                .map(in -> {
                    Material m = in.getMaterial();
                    if (m == null) return null;

                    Integer spec = m.getSpec() != null ? m.getSpec() : 0;
                    String scale = m.getScale() != null ? m.getScale() : "";

                    // 🔹 LOT별 남은 재고
                    int lotStock = in.getStock() != null ? in.getStock() : 0;

                    // 🔹 품목별 전체 재고
                    int totalStock = materialStockRepository.findByMaterial(m)
                            .map(MaterialStock::getStock)
                            .orElse(0);

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
                            .stock(lotStock) // ✅ LOT별 재고량
                            .totalStock(in.getInAmount()*spec) // ✅ 전체 재고량
                            .build();
                })
                .filter(Objects::nonNull)
                .toList();
    }


    //삭제
    public void softDelete(Long id) {
        MaterialIn materialIn = materialInRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("입고 데이터를 찾을 수 없습니다."));

        if (materialIn.getDelYn() == Yn.Y)
            throw new IllegalStateException("이미 삭제된 데이터입니다.");

        materialIn.setDelYn(Yn.Y);

        // 🔹 재고 차감
        MaterialStock stock = materialStockRepository.findByMaterial(materialIn.getMaterial())
                .orElseThrow(() -> new EntityNotFoundException("재고 정보를 찾을 수 없습니다."));
        stock.setStock(stock.getStock() - materialIn.getInAmount());
        if (stock.getStock() < 0) stock.setStock(0);
        materialStockRepository.save(stock);


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
                        "MINC-" + inDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                );
            }

            // 4️⃣ LOT 번호 생성
            String lotNum = String.format("MINC-%s-%03d",
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
                    .stock(dto.getInAmount())
                    .delYn(Yn.N)
                    .build();

            // 6️⃣ 저장
            materialInRepository.save(materialIn);

            MaterialStock stock = materialStockRepository.findByMaterial(material)
                    .orElse(MaterialStock.builder()
                            .material(material)
                            .stock(0)
                            .build());

            stock.setStock(stock.getStock() + dto.getInAmount());
            materialStockRepository.save(stock);
        }
    }

    public void updateRegisterIn(MaterialInDto dto) {
        MaterialIn materialIn = materialInRepository.findById(dto.getId())
                .orElseThrow(() -> new EntityNotFoundException("입고를 찾을 수 없습니다."));

        int oldAmount = materialIn.getInAmount();
        int newAmount = Optional.ofNullable(dto.getInAmount()).orElse(oldAmount);

        if (newAmount != oldAmount) {
            Material material = materialIn.getMaterial();

            // ✅ 1. LOT(입고 단위) 재고 수정
            int oldStock = Optional.ofNullable(materialIn.getStock()).orElse(0);
            materialIn.setStock(oldStock + newAmount - oldAmount); // ✅ (+new - old)
            materialInRepository.save(materialIn);

            // ✅ 2. 품목별 전체 재고 수정
            MaterialStock stock = materialStockRepository.findByMaterial(material)
                    .orElseThrow(() -> new EntityNotFoundException("재고 정보를 찾을 수 없습니다."));
            stock.setStock(stock.getStock() + newAmount - oldAmount); // ✅ (+new - old)
            materialStockRepository.save(stock);
        }

        // ✅ 3. 기본 정보 갱신
        if (dto.getInAmount() != null) materialIn.setInAmount(dto.getInAmount());
        if (dto.getInDate() != null) materialIn.setInDate(dto.getInDate());
        if (dto.getManufactureDate() != null) materialIn.setManufactureDate(dto.getManufactureDate());

        materialInRepository.save(materialIn);
    }


}
