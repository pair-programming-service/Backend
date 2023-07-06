package com.pair.website.controller;

import com.pair.website.domain.Chat;
import com.pair.website.dto.ChatRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatController {
    private final SimpMessageSendingOperations sendingOperations;

    @MessageMapping("/chat/message/{roomId}")
    public void message(@DestinationVariable String roomId, ChatRequestDto chatRequestDto){
        log.info("message : {}", chatRequestDto.getMessage());
        sendingOperations.convertAndSend("/sub/room/" + roomId,chatRequestDto);
    }
}
