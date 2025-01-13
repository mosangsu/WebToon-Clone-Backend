package com.lezhin.clone.backend.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.Objects;

import org.springframework.lang.NonNull;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum RankingType {
    NEW("신작", "N"),
    EVENT("이벤트", "E"),
    DEFAULT("일반", "D");

    private final String value;
    private final String code;

    RankingType(String value, String code) {
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
    
    public static RankingType ofName(String name) {
        return Arrays.stream(values()).filter(r -> r.getName().equals(name)).findAny().orElse(DEFAULT);
    }
    
    public static RankingType ofCode(String code) {
        return Arrays.stream(values()).filter(r -> r.getCode().equals(code)).findAny().orElse(DEFAULT);
    }

    @Converter
    public static class RankingTypeAttributeConverter implements AttributeConverter<RankingType, String> {
    
        @Override
        public String convertToDatabaseColumn(RankingType attribute) {
            if (Objects.isNull(attribute)) {
                return RankingType.DEFAULT.getCode();
            }
            return attribute.getCode();
        }
    
        @Override
        public RankingType convertToEntityAttribute(String dbData) {
            return RankingType.ofCode(dbData); // find는 미리 정의된 함수. 없으면 Exception.
       }  
    }

    public static class RankingTypeConverter implements org.springframework.core.convert.converter.Converter<String, RankingType> {

        @Override
        public RankingType convert(@NonNull String source) {
            return RankingType.ofCode(source);
        }
        
    }
}
