package com.lezhin.clone.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class CoinDto {

    public static class CoinDetails {
        @Getter
        @Setter
        @NoArgsConstructor
        public static class Res {
            private Long coinId;
            private Long coinAccumId;
            private Integer amount;
        }
    }

    public static class Amounts {

        @Getter
        @Setter
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Res {
            private Integer coinAmount;
            private Integer bonusCoinAmount;
            private Integer pointAmount;
        }
    }
}