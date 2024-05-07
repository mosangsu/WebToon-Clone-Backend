package com.lezhin.clone.backend.dto;

import com.lezhin.clone.backend.enums.MemberType;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
                private String username;
                private MemberType type;
                @Builder
                public User(Long id, String username, MemberType type) {
                    this.id = id;
                    this.username = username;
                    this.type = type;
                }
            }
        }
    }

    public static class OAuth2 {
        public static class Res {
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
}