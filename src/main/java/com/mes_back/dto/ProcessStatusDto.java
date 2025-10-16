package com.mes_back.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcessStatusDto {
    private Long id;
    private String processCode;
    private String processName;
    private Integer processTime; // 분
    private String remark;
    private String completedStatus; // "대기", "진행중", "완료"
    private String startTime;
}
