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
    public String inNum;
    public Date outDate;
    public String outNum;
    public String delYn;
    public String materialCode;
    private String materialName;
    private String manufacturer;
    private String companyName;
    private Integer spec;
    private String scale;
    private String specAndScale;
    private Date manufactureDate;

}
