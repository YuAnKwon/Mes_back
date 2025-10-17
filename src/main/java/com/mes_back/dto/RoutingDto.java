package com.mes_back.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoutingDto {
    private Long id; // 라우팅 id
    private String processCode; // 공정코드
    private String processName; // 공정명
    private Integer processTime; // 공정시간
    private String remark; //비고
}
