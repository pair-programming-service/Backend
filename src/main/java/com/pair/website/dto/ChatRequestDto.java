package com.pair.website.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatRequestDto {
    private String roomId;
    private String sender;
    private String message;
}
