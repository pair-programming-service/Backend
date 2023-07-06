package com.pair.website.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatRequestDto {
    private String message;
}
