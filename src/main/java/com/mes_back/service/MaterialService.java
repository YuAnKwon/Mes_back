package com.mes_back.service;
import com.mes_back.constant.MaterialType;
import com.mes_back.constant.Yn;
import com.mes_back.dto.CompanyDto;
import com.mes_back.dto.MaterialDto;
import com.mes_back.dto.MaterialListDto;
import com.mes_back.entity.Company;
import com.mes_back.entity.Material;
import com.mes_back.entity.OrderItem;
import com.mes_back.repository.CompanyRepository;
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
    private final CompanyRepository companyRepository;

    //register and update
    //업체 등록(Dto에서 받은 값을 새로 생성한 Material(엔티티) 객체에 넣기 ==> DB에 저장)
    @Transactional
    public Material saveMaterial(Long id, MaterialDto dto) {
        Material material;

        if (id == null) {
            // 신규 등록
            material = new Material();

            if(materialRepository.existsByCode(dto.getMaterialCode())){
                throw new IllegalArgumentException("이미 존재하는 품목번호입니다.");
            }
        } else {
            // 수정
            material = materialRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("해당 업체가 존재하지 않습니다."));
        }

        System.out.println("입력된 회사명: " + dto.getCompanyName());

        // String → Company 엔티티 변환
        Company company = companyRepository.findByCompanyName(dto.getCompanyName())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회사입니다."));


        // 공통 필드 설정
        material.setCode(dto.getMaterialCode());
        material.setName(dto.getMaterialName());
        material.setCompany(company);
        material.setType(MaterialType.valueOf(dto.getType()));
        material.setColor(dto.getColor());
        material.setSpec(dto.getSpec());
        material.setScale(dto.getScale());
        material.setManufacturer(dto.getManufacturer());
        material.setRemark(dto.getRemark());
        material = materialRepository.save(material);

        return materialRepository.save(material);
    }


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

    //원자재 상세페이지 조회
    public MaterialDto getMaterialDetail(Long id) {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 업체가 존재하지 않습니다."));
        return MaterialDto.fromEntity(material);
    }

}
