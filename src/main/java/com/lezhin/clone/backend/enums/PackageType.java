package com.lezhin.clone.backend.enums;

import java.util.Arrays;
import java.util.Objects;

import org.springframework.lang.NonNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;


@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum PackageType {
    NORMAL("일반", "N"),
    FIRST_PURCHASE("첫결제", "F"),
    POINT("포인트", "P");

    private final String value;
    private final String code;

    PackageType(String value, String code) {
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
    
    public static PackageType ofName(String name) {
        return Arrays.stream(values()).filter(r -> r.getName().equals(name)).findAny().orElse(NORMAL);
    }
    
    public static PackageType ofCode(String code) {
        return Arrays.stream(values()).filter(r -> r.getCode().equals(code)).findAny().orElse(NORMAL);
    }

    @Converter
    public static class PackageTypeAttributeConverter implements AttributeConverter<PackageType, String> {
    
        @Override
        public String convertToDatabaseColumn(PackageType attribute) {
            if (Objects.isNull(attribute)) {
                return PackageType.NORMAL.getCode();
            }
            return attribute.getCode();
        }
    
        @Override
        public PackageType convertToEntityAttribute(String dbData) {
            return PackageType.ofCode(dbData); // find는 미리 정의된 함수. 없으면 Exception.
       }  
    }

    public static class PackageTypeConverter implements org.springframework.core.convert.converter.Converter<String, PackageType> {

        @Override
        public PackageType convert(@NonNull String source) {
            return PackageType.ofCode(source);
        }
        
    }
}
