package com.lezhin.clone.backend.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OAuth2Provider {
    GOOGLE("google"),
    GITHUB("github"),
    NAVER("naver"),
    KAKAO("kakao"),
    NONE("none");

    private final String registrationId;
}