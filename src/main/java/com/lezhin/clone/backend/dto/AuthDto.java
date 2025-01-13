package com.lezhin.clone.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class AuthDto {
    public static class EmailVerification {
        @Getter
        @Setter
        @NoArgsConstructor
        public static class Req {
            private String email;
        }
    }
}