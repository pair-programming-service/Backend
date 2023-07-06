package com.pair.website.dto.response;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class ChatRoomResponseDto {
    private String roomId;
    private String member1;
    private String member2;
}
