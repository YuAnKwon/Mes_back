package com.mes_back.constant;

public class EnumKoreanMapper {

    public static String getOrderItemTypeKorean(String type) {
        switch (OrderItemType.valueOf(type)) {
            case GENERAL: return "일반";
            case CAR: return "자동차";
            case SHIPBUILDING: return "조선";
            case DEFENSE: return "방산";
            default: return "기타";
        }
    }

    public static String getCoatingMethodKorean(String method) {
        switch (CoatingMethod.valueOf(method)) {
            case POWDER: return "분체";
            case LIQUID: return "액체";
            default: return "기타";
        }
    }

    public static String getMaterialTypeKorean(String type) {
        switch (MaterialType.valueOf(type)) {
            case PAINT: return "페인트";
            case THINNER: return "신너";
            case CLEANER: return "세정제";
            case HARDENER: return "경화제";
            default: return "기타";
        }
    }

    public static String getCompanyTypeKorean(String type) {
        switch (CompanyType.valueOf(type)) {
            case CLIENT: return "거래처";
            case SUPPLIER: return "매입처";
            default: return "기타";
        }
    }
    public static String getBusinessYnKorean(String type) {
        switch (Yn.valueOf(type)) {
            case Y: return "거래 중";
            case N: return "거래 종료";
            default: return "기타";
        }
    }
}

