package com.pair.website.dto;

import com.pair.website.domain.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatRoomRequestDto {
    private Long customer;
}
