package com.pair.website.controller;

import com.pair.website.domain.Chat;
import com.pair.website.domain.Member;
import com.pair.website.dto.ChatRequestDto;
import com.pair.website.dto.response.BaseResponseDto;
import com.pair.website.service.ChatService;
import com.pair.website.util.PublicMethod;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatController {
    private final SimpMessageSendingOperations sendingOperations;
    private final ChatService chatService;
    private final PublicMethod publicMethod;
    @MessageMapping("/chat/message")
    public BaseResponseDto<?> message(ChatRequestDto chatRequestDto){
//        BaseResponseDto<?> result = publicMethod.checkLogin(request);
//        if (!result.isSuccess()) return result;
//        Member member = (Member) result.getData();
        sendingOperations.convertAndSend("/sub/room/" + chatRequestDto.getRoomId(),chatRequestDto);
        return chatService.saveChat(chatRequestDto);
    }
}
