package com.pair.website.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

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

    @AllArgsConstructor
    @Getter
    public static class Page {
        private int page;
        private List<BoardAllResponseDto> boardAllResponseDtoList;
    }


    public static <T> BaseResponseDto<T> success(T data) {

        return new BaseResponseDto<T>(true, data, null);
    }

    public static <T> BaseResponseDto<T> fail(String code, String message) {
        return new BaseResponseDto<T>(false, null, new Error(code, message));
    }

    public static <T> BaseResponseDto<T> page(int page, List<BoardAllResponseDto> boardAllResponseDtoList) {
        return new BaseResponseDto<T>(true, (T) new Page(page,boardAllResponseDtoList),null);
    }
}
