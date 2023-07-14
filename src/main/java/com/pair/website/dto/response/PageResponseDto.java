package com.pair.website.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PageResponseDto<T> {
    private boolean success;
    private int page;
    private T data; //제네릭의 변수 선언방법
    private Error error;


    public static <T> PageResponseDto<T> success(T data,int page) {

        return new PageResponseDto<T>(true, page, data,null);
    }
}