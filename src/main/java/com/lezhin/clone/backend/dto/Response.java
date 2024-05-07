package com.lezhin.clone.backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Response {
    String code;
    String message;
    Object data;

    @Builder
    public Response(String code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}