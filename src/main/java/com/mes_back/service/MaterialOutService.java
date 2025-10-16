package com.mes_back.service;

import com.mes_back.constant.Yn;
import com.mes_back.dto.MaterialDto;
import com.mes_back.dto.MaterialInDto;
import com.mes_back.dto.MaterialOutDto;
import com.mes_back.entity.Material;
import com.mes_back.entity.MaterialIn;
import com.mes_back.entity.MaterialOut;
import com.mes_back.entity.MaterialStock;
import com.mes_back.repository.MaterialInRepository;
import com.mes_back.repository.MaterialOutRepository;
import com.mes_back.repository.MaterialStockRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MaterialOutService {
    private final MaterialInRepository materialInRepository;
    private final MaterialStockRepository materialStockRepository;
    private final MaterialOutRepository materialOutRepository;

    public List<MaterialDto> findAllOutRegister() {
        List<MaterialIn> inList = materialInRepository.findByDelYn(Yn.N);

        // 품목별로 그룹핑
        Map<Material, List<MaterialIn>> grouped = inList.stream()
                .filter(in -> in.getMaterial() != null)
                .collect(Collectors.groupingBy(MaterialIn::getMaterial));

        // DTO로 변환
        return grouped.entrySet().stream()
                .map(entry -> {
                    Material material = entry.getKey();
                    if (material == null) return null;

                    int stock = materialStockRepository.findByMaterial(material)
                            .map(MaterialStock::getStock)
                            .orElse(0);

                    return MaterialDto.builder()
                            .id(material.getId())
                            .materialName(material.getName())
                            .materialCode(material.getCode())
                            .companyName(material.getCompany() != null ? material.getCompany().getCompanyName() : "")
                            .manufacturer(material.getManufacturer())
                            .scale(material.getScale())
                            .spec(material.getSpec())
                            .specAndScale(material.getSpec() + material.getScale())

                            .stock(stock)
                            .build();
                })
                .toList();
    }

    public void registerOut(List<MaterialOutDto> outList) {

        // 날짜별 출고 LOT 번호 관리용 Map
        Map<LocalDate, Long> outCountMap = new HashMap<>();

        for (MaterialOutDto dto : outList) {

            // 1️⃣ LOT(입고번호) 기준으로 입고 데이터 조회
            MaterialIn materialIn = materialInRepository.findByInNum(dto.getInNum())
                    .orElseThrow(() -> new EntityNotFoundException("입고번호를 찾을 수 없습니다: " + dto.getInNum()));

            Material material = materialIn.getMaterial();
            int outAmount = dto.getOutAmount();

            // 2️⃣ LOT별 재고 차감
            int currentStock = materialIn.getStock() != null ? materialIn.getStock() : 0;
            if (currentStock < outAmount) {
                throw new IllegalArgumentException("출고 수량이 남은 재고보다 많습니다. (남은 재고: " + currentStock + ")");
            }
            materialIn.setStock(currentStock - outAmount);
            materialInRepository.save(materialIn);

            // 3️⃣ 품목별 총 재고 차감
            MaterialStock materialStock = materialStockRepository.findByMaterial(material)
                    .orElseThrow(() -> new EntityNotFoundException("품목별 재고를 찾을 수 없습니다."));
            materialStock.setStock(materialStock.getStock() - outAmount);
            materialStockRepository.save(materialStock);

            // 4️⃣ 출고일자 → LocalDate 변환
            LocalDate outDate = dto.getOutDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            // 5️⃣ LOT 번호 카운트 확인
            long count;
            if (outCountMap.containsKey(outDate)) {
                count = outCountMap.get(outDate);
            } else {
                // DB에서 오늘 날짜로 시작하는 출고번호 개수 확인
                count = materialOutRepository.countByOutNumStartingWith(
                        "OUT-" + outDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                );
            }

            // 6️⃣ LOT 번호 생성
            String outNum = String.format("OUT-%s-%03d",
                    outDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")),
                    count + 1);

            // Map 순번 갱신
            outCountMap.put(outDate, count + 1);

            // 7️⃣ 출고 이력 저장
            MaterialOut out = MaterialOut.builder()
                    .material(material)
                    .materialIn(materialIn)
                    .outAmount(outAmount)
                    .outDate(dto.getOutDate())
                    .outNum(outNum)
                    .build();

            materialOutRepository.save(out);
        }
    }
}
