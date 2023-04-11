package com.pair.website.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BaseResponseDto<T> {

    private boolean success;
    private T data; //제네릭의 변수 선언방법
    private Error error;

    @AllArgsConstructor
    @Getter
    public static class Error {

        private String code;
        private String message;
    }


    public static <T> BaseResponseDto<T> success(T data) {

        return new BaseResponseDto<T>(true, data, null);
    }

    public static <T> BaseResponseDto<T> fail(String code, String message) {
        return new BaseResponseDto<T>(false, null, new Error(code, message));
    }
}
