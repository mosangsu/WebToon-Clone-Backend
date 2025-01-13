package com.lezhin.clone.backend.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.Objects;

import org.springframework.lang.NonNull;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum CoinType {
    COIN("코인", "C"),
    BONUSCOIN("보너스코인", "B"),
    POINT("포인트", "P"),
    ALL("전체", "A");

    private final String value;
    private final String code;

    CoinType(String value, String code) {
        this.value = value;
        this.code = code;
    }

    public String getName() {
        return name();
    }

    public String getValue() {
        return this.value;
    }

    public String getCode() {
        return this.code;
    }
    
    public static CoinType ofName(String name) {
        return Arrays.stream(values()).filter(r -> r.getName().equals(name)).findAny().orElse(COIN);
    }
    
    public static CoinType ofCode(String code) {
        return Arrays.stream(values()).filter(r -> r.getCode().equals(code)).findAny().orElse(COIN);
    }

    @Converter
    public static class CoinTypeAttributeConverter implements AttributeConverter<CoinType, String> {
    
        @Override
        public String convertToDatabaseColumn(CoinType attribute) {
            if (Objects.isNull(attribute)) {
                return CoinType.COIN.getCode();
            }
            return attribute.getCode();
        }
    
        @Override
        public CoinType convertToEntityAttribute(String dbData) {
            return CoinType.ofCode(dbData); // find는 미리 정의된 함수. 없으면 Exception.
       }  
    }

    public static class CoinTypeConverter implements org.springframework.core.convert.converter.Converter<String, CoinType> {

        @Override
        public CoinType convert(@NonNull String source) {
            return CoinType.ofCode(source);
        }
    }
}
