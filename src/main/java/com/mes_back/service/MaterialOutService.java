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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MaterialOutService {
    private final MaterialInRepository materialInRepository;
    private final MaterialStockRepository materialStockRepository;
    private final MaterialOutRepository materialOutRepository;

    public List<MaterialInDto> findAllOutRegister() {
        return materialInRepository.findByDelYn(Yn.N).stream()
                .filter(in -> in.getMaterial() != null)
                .map(in -> {
                    Material m = in.getMaterial();
                    return MaterialInDto.builder()
                            .id(in.getId())
                            .inNum(in.getInNum()) // ✅ LOT(입고번호)
                            .materialCode(m.getCode())
                            .materialName(m.getName())
                            .companyName(m.getCompany() != null ? m.getCompany().getCompanyName() : "")
                            .manufacturer(m.getManufacturer())
                            .spec(m.getSpec())
                            .scale(m.getScale())
                            .specAndScale(m.getSpec() + m.getScale())
                            .inAmount(in.getInAmount())
                            .stock(in.getStock()) // ✅ 남은 재고
                            .inDate(in.getInDate())
                            .manufactureDate(in.getManufactureDate())
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
                        "MOUT-" + outDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                );
            }

            // 6️⃣ LOT 번호 생성
            String outNum = String.format("MOUT-%s-%03d",
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
                    .delYn(dto.getDelYn() != null ? Yn.valueOf(dto.getDelYn()) : Yn.N)
                    .build();

            materialOutRepository.save(out);
        }
    }

    //
    public List<MaterialOutDto> findAllActive() {
        return materialOutRepository.findByDelYn(Yn.N).stream()
                .map(out -> {
                    Material material = out.getMaterial();
                    MaterialIn materialIn = out.getMaterialIn();

                    return MaterialOutDto.builder()
                            .id(out.getId())
                            .outNum(out.getOutNum())
                            .outDate(out.getOutDate())
                            .outAmount(out.getOutAmount())
                            .materialCode(material != null ? material.getCode() : "")
                            .materialName(material != null ? material.getName() : "")
                            .manufacturer(material != null ? material.getManufacturer() : "")
                            .companyName(
                                    material != null && material.getCompany() != null
                                            ? material.getCompany().getCompanyName()
                                            : ""
                            )
                            .spec(material != null ? material.getSpec() : 0)
                            .scale(material != null ? material.getScale() : "")
                            .specAndScale(
                                    material != null
                                            ? material.getSpec() + material.getScale()
                                            : ""
                            )
                            .inNum(materialIn != null ? materialIn.getInNum() : "")
                            .manufactureDate(materialIn != null ? materialIn.getManufactureDate() : null)
                            .delYn(out.getDelYn() != null ? out.getDelYn().name() : "N")
                            .build();
                })
                .toList();
    }

    public void updateRegisterOut(MaterialOutDto dto) {
        MaterialOut materialOut = materialOutRepository.findById(dto.getId())
                .orElseThrow(() -> new EntityNotFoundException("출고를 찾을 수 없습니다."));

        int oldAmount = materialOut.getOutAmount();
        int newAmount = Optional.ofNullable(dto.getOutAmount()).orElse(oldAmount);

        if (newAmount != oldAmount) {
            Material material = materialOut.getMaterial();
            MaterialIn materialIn = materialOut.getMaterialIn();

            // ✅ 1. MaterialStock 조정 (전체 재고)
            MaterialStock stock = materialStockRepository.findByMaterial(material)
                    .orElseThrow(() -> new EntityNotFoundException("품목별 재고를 찾을 수 없습니다."));
            stock.setStock(stock.getStock() + oldAmount - newAmount); // ✅ (+old - new)
            materialStockRepository.save(stock);

            // ✅ 2. MaterialIn 조정 (해당 LOT 재고)
            if (materialIn != null) {
                materialIn.setStock(materialIn.getStock() + oldAmount - newAmount); // ✅ (+old - new)
                materialInRepository.save(materialIn);
            }
        }

        if (dto.getOutAmount() != null) materialOut.setOutAmount(dto.getOutAmount());
        if (dto.getOutDate() != null) materialOut.setOutDate(dto.getOutDate());

        materialOutRepository.save(materialOut);
    }

    public void softDelete(Long id) {
        MaterialOut materialOut = materialOutRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("입고 데이터를 찾을 수 없습니다."));

        if (materialOut.getDelYn() == Yn.Y)
            throw new IllegalStateException("이미 삭제된 데이터입니다.");

        materialOut.setDelYn(Yn.Y);

        // 🔹 재고 차감
        MaterialStock stock = materialStockRepository.findByMaterial(materialOut.getMaterial())
                .orElseThrow(() -> new EntityNotFoundException("재고 정보를 찾을 수 없습니다."));
        stock.setStock(stock.getStock() + materialOut.getOutAmount());
        if (stock.getStock() < 0) stock.setStock(0);
        materialStockRepository.save(stock);

        MaterialIn materialIn = materialOut.getMaterialIn();
        if (materialIn != null) {
            int currentLotStock = materialIn.getStock() != null ? materialIn.getStock() : 0;
            materialIn.setStock(currentLotStock + materialOut.getOutAmount());
            materialInRepository.save(materialIn);
        }
    }

}
