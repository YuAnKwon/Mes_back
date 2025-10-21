package com.mes_back.dto;

import com.mes_back.constant.EnumKoreanMapper;
import com.mes_back.constant.Yn;
import com.mes_back.entity.Company;
import com.mes_back.entity.Material;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class MaterialListDto {
    private Long id;
    private String materialName; // 품목명
    private String materialCode; // 품목번호
    private String companyName; // 매입처명
    private String type;  // 분류 (ENUM)
    private String color;  // 색상
    private String useYn = "Y";  // 사용여부 (Y/N)
    private String remark; // 비고

    //DB에서 가져온 값을 Dto에 복사
    public MaterialListDto(Material material) {
        this.id =  material.getId();
        this.materialName = material.getName();
        this.materialCode = material.getCode();
        this.companyName = material.getCompany().getCompanyName();
        this.type =  EnumKoreanMapper.getMaterialTypeKorean(material.getType().name());
        this.color = material.getColor();
        this.useYn = EnumKoreanMapper.getBusinessYnKorean(material.getUseYn().name());
        this.remark = material.getRemark();

    }



}
