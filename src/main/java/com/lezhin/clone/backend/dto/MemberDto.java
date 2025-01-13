package com.lezhin.clone.backend.dto;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lezhin.clone.backend.enums.CoinStatus;
import com.lezhin.clone.backend.enums.CoinType;
import com.lezhin.clone.backend.enums.MemberType;
import com.lezhin.clone.backend.enums.OAuth2Provider;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class MemberDto {
    public static class Auth {

        @Getter
        @Setter
        public static class Req {
            private String username;
            private String password;
        }

        @Getter
        @Setter
        @NoArgsConstructor
        public static class Res {
            private User user;
            private String token;

            @Builder
            public Res(User user, String token) {
                this.user = user;
                this.token = token;
            }

            @Getter
            @Setter
            @NoArgsConstructor
            public static class User {
                private Long id;
                private String nickname;
                private MemberType type;
                @Builder
                public User(Long id, String nickname, MemberType type) {
                    this.id = id;
                    this.nickname = nickname;
                    this.type = type;
                }
            }
        }
    }

    public static class OAuth2 {
        @Getter
        @Setter
        public static class Req {
            String id;
            String name;
            String email;
            String profileImageUrl;
            OAuth2Provider provider;
        }
    }

    public static class SignUp {

        @Getter
        @Setter
        public static class Req {
            String username;
            String password;
            String birthYear;
            String birthMonth;
            String birthDay;
            String gender;
        }
    }
    public static class Account {

        @Getter
        @Setter
        public static class Res {
            private Long memberId;
            private String username;
            private String password;
            private MemberType type;
            private String nickname;
        }
    }

    public static class Coin {
        @Getter
        @Setter
        public static class Res {
            private Long memberId;
            private MemberType type;
            private String nickname;
            private Integer coinAmount;
            private Integer bonusCoinAmount;
            private Integer pointAmount;
        }
    }

    public static class ExpiringCoins {
        
        @Getter
        @Setter
        public static class Res {
            private Long coinId;
            private CoinStatus status;
            private CoinType type;
            private int amount;
            private String description;
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
            private LocalDateTime expirationDate;
        }
    }

    public static class ChargeHistory {
        
        @Getter
        @Setter
        public static class Res {
            private Long coinId;
            private CoinStatus status;
            private CoinType type;
            private int amount;
            private String description;
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
            private LocalDateTime expirationDate;
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
            private LocalDateTime createdAt;
        }
    }

    public static class UsageHistory {
        
        @Getter
        @Setter
        public static class Res {
            private Long coinId;
            private CoinStatus status;
            private CoinType type;
            private int amount;
            private String description;
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
            private LocalDateTime expirationDate;
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
            private LocalDateTime createdAt;
        }
    }
}