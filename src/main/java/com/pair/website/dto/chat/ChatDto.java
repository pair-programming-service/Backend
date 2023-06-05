package com.pair.website.dto.chat;

import lombok.*;


@Data
@Builder
public class ChatDto {

    //메시지 타입 :  입장 채팅
    public enum MessageType {
        ENTER, TALK
    }

    private MessageType type; //메시지 타입
    private String roomId;// 방 번호
    private String sender;//채팅을 보낸 사람
    private String message;// 메세지
    private String time; // 채팅 발송 시간

    public ChatDto() {
        
    }

    public ChatDto(MessageType type, String roomId, String sender, String message, String time) {
        this.type = type;
        this.roomId = roomId;
        this.sender = sender;
        this.message = message;
        this.time = time;
    }
}