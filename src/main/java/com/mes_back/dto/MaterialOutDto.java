package com.mes_back.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class MaterialOutDto {
    public Long id;
    public Integer outAmount;
    public String InNum;
    public Date outDate;
    public String outNum;
}
