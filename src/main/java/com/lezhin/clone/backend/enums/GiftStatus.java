package com.lezhin.clone.backend.enums;

import java.util.Arrays;
import java.util.Objects;

import org.springframework.lang.NonNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;


@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum GiftStatus {
    CLAIMED("수령", "C"),
    UNCLAIMED("미수령", "U"),
    EXPIRE("만료", "E");

    private final String value;
    private final String code;

    GiftStatus(String value, String code) {
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
    
    public static GiftStatus ofName(String name) {
        return Arrays.stream(values()).filter(r -> r.getName().equals(name)).findAny().orElse(UNCLAIMED);
    }
    
    public static GiftStatus ofCode(String code) {
        return Arrays.stream(values()).filter(r -> r.getCode().equals(code)).findAny().orElse(UNCLAIMED);
    }

    @Converter
    public static class GiftStatusAttributeConverter implements AttributeConverter<GiftStatus, String> {
    
        @Override
        public String convertToDatabaseColumn(GiftStatus attribute) {
            if (Objects.isNull(attribute)) {
                return GiftStatus.UNCLAIMED.getCode();
            }
            return attribute.getCode();
        }
    
        @Override
        public GiftStatus convertToEntityAttribute(String dbData) {
            return GiftStatus.ofCode(dbData); // find는 미리 정의된 함수. 없으면 Exception.
       }  
    }

    public static class GiftStatusConverter implements org.springframework.core.convert.converter.Converter<String, GiftStatus> {

        @Override
        public GiftStatus convert(@NonNull String source) {
            return GiftStatus.ofCode(source);
        }
        
    }
}
