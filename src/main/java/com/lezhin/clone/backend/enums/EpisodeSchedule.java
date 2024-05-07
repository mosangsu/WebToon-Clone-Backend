package com.lezhin.clone.backend.enums;

import java.util.Arrays;
import java.util.Objects;

import org.springframework.lang.NonNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum EpisodeSchedule {
    MON("월", "1"),
    TUE("화", "2"),
    WED("수", "3"),
    THU("목", "4"),
    FRI("금", "5"),
    SAT("토", "6"),
    SUN("일", "0"),
    TEN("열흘", "n"),
    COM("연재종료", "c"),
    UNKNOWN("알수없음", "u");

    private final String value;

    private final String code;

    EpisodeSchedule(String value, String code) {
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
    
    public static EpisodeSchedule ofName(String name) {
        return Arrays.stream(values()).filter(episodeSchedule -> episodeSchedule.getName().equals(name)).findAny().orElse(UNKNOWN);
    }
    
    public static EpisodeSchedule ofCode(String code) {
        return Arrays.stream(values()).filter(episodeSchedule -> episodeSchedule.getCode().equals(code)).findAny().orElse(UNKNOWN);
    }

    @Converter
    public static class EpisodeScheduleAttributeConverter implements AttributeConverter<EpisodeSchedule, String> {
    
        @Override
        public String convertToDatabaseColumn(EpisodeSchedule attribute) {
            if (Objects.isNull(attribute)) {
                return EpisodeSchedule.UNKNOWN.getCode();
            }
            return attribute.getCode();
        }
    
        @Override
        public EpisodeSchedule convertToEntityAttribute(String dbData) {
            return EpisodeSchedule.ofCode(dbData);
       }  
    }

    public static class EpisodeScheduleConverter implements org.springframework.core.convert.converter.Converter<String, EpisodeSchedule> {

        @Override
        public EpisodeSchedule convert(@NonNull String source) {
            return EpisodeSchedule.ofCode(source);
        }
        
    }
}
