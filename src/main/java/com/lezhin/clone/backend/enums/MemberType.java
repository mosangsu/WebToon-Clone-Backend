package com.lezhin.clone.backend.enums;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonFormat;


@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum MemberType {
    USER("일반", Authority.USER),
    ADMIN("관리자", Authority.ADMIN),
    WITHDRAWAL("탈퇴", Authority.WITHDRAWAL),
    UNKNOWN("알수없음", Authority.UNKNOWN);

    private final String value;

    private final String authority;

    MemberType(String value, String authority) {
        this.value = value;
        this.authority = authority;
    }

    public String getName() {
        return name();
    }

    public String getValue() {
        return this.value;
    }

    public String getAuthority() {
        return this.authority;
    }

    public static class Authority {
        public static final String USER = "ROLE_USER";
        public static final String ADMIN = "ROLE_ADMIN";
        public static final String WITHDRAWAL = "ROLE_WITHDRAWAL";
        public static final String UNKNOWN = "UNKNOWN";
    }
    
    public static MemberType find(String authority) {
        return Arrays.stream(values()).filter(memberType -> memberType.getAuthority().equals(authority)).findAny().orElse(UNKNOWN);
    }
}
