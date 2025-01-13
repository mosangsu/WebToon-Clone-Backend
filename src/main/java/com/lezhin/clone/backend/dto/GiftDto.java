package com.lezhin.clone.backend.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lezhin.clone.backend.enums.GiftStatus;

import lombok.Getter;
import lombok.Setter;

public class GiftDto {
    public static class List {

        @Getter
        @Setter
        public static class Res {
            private Long giftId;
            private GiftStatus status;
            private int coins;
            private int bonusCoins;
            private int points;
            private String title;
            private String description;
            private String imageUrl;
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
            private LocalDateTime expirationDate;
        }
    }
}