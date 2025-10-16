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
    private String name; // 품목명
    private String code; // 품목번호
    private String company; // 매입처명
    private String type;  // 분류 (ENUM)
    private String color;  // 색상
    private Yn useYn;  // 사용여부 (Y/N)
    private String remark; // 비고

    //DB에서 가져온 값을 Dto에 복사
    public MaterialListDto(Material material) {
        this.id =  material.getId();
        this.name = material.getName();
        this.code = material.getCode();
        this.company = material.getCompany().getCompanyName();
        this.type =  material.getType().name();
        this.color = material.getColor();
        this.useYn = material.getUseYn();
        this.remark = material.getRemark();

    }

}
