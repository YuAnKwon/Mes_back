package com.mes_back.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcessStatusDto {
    private Long id;
    private Integer routingOrder;
    private String processCode;
    private String processName;
    private Integer processTime;
    private String remark;
    private String completedStatus;
    private String startTime;
}





