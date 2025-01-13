package com.lezhin.clone.backend.enums;

import java.util.Arrays;
import java.util.Objects;

import org.springframework.lang.NonNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;


@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum CoinStatus {
    ACCUMULATE("적립", "A"),
    USE("사용", "U"),
    EXPIRE("만료", "E");

    private final String value;
    private final String code;

    CoinStatus(String value, String code) {
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
    
    public static CoinStatus ofName(String name) {
        return Arrays.stream(values()).filter(r -> r.getName().equals(name)).findAny().orElse(ACCUMULATE);
    }
    
    public static CoinStatus ofCode(String code) {
        return Arrays.stream(values()).filter(r -> r.getCode().equals(code)).findAny().orElse(ACCUMULATE);
    }

    @Converter
    public static class CoinStatusAttributeConverter implements AttributeConverter<CoinStatus, String> {
    
        @Override
        public String convertToDatabaseColumn(CoinStatus attribute) {
            if (Objects.isNull(attribute)) {
                return CoinStatus.ACCUMULATE.getCode();
            }
            return attribute.getCode();
        }
    
        @Override
        public CoinStatus convertToEntityAttribute(String dbData) {
            return CoinStatus.ofCode(dbData); // find는 미리 정의된 함수. 없으면 Exception.
       }  
    }

    public static class CoinStatusConverter implements org.springframework.core.convert.converter.Converter<String, CoinStatus> {

        @Override
        public CoinStatus convert(@NonNull String source) {
            return CoinStatus.ofCode(source);
        }
        
    }
}
