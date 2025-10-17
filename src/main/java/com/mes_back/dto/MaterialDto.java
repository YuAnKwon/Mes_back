package com.mes_back.dto;

import com.mes_back.entity.Company;
import com.mes_back.entity.Material;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class MaterialDto {
    private Long id;
    private String materialName; // 품목명
    private String materialCode; // 품목번호
    private String companyName; // 매입처명
    private String type;  // 분류 (ENUM)
    private String color;  // 색상
    private Integer spec; // 규격
    private String scale; // 단위
    private String manufacturer; // 제조사
    private String remark; // 비고
    private String specAndScale; // 원자재 규격 + 단위
    private Integer stock;


    public static MaterialDto fromEntity(Material material) {
        return MaterialDto.builder()
                .id(material.getId())
                .materialCode(material.getCode())
                .materialName(material.getName())
                .companyName(material.getCompany().getCompanyName())
                .type(material.getType().name())
                .color(material.getColor())
                .spec(material.getSpec())
                .scale(material.getScale())
                .manufacturer(material.getManufacturer())
                .remark(material.getRemark())
                .build();
    }
}
