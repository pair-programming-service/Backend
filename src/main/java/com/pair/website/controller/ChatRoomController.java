package com.pair.website.controller;

import com.pair.website.domain.Member;
import com.pair.website.dto.response.BaseResponseDto;
import com.pair.website.service.ChatService;
import com.pair.website.util.PublicMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class ChatRoomController {
    private final ChatService chatService;
    private final PublicMethod publicMethod;

    @PostMapping("/room/create/{nickname}")
    public BaseResponseDto<?> create(@PathVariable String nickname, HttpServletRequest request){
        BaseResponseDto<?> result = publicMethod.checkLogin(request);
        if (!result.isSuccess()) return result;
        Member member = (Member) result.getData();
        return chatService.createRoom(nickname,member);
    }

    @GetMapping("/room")
    public BaseResponseDto<?> findRoom(HttpServletRequest request){
        BaseResponseDto<?> result = publicMethod.checkLogin(request);
        if (!result.isSuccess()) return result;
        Member member = (Member) result.getData();
        return chatService.findRoom(member);
    }

    @GetMapping("/room/{roomId}")
    public BaseResponseDto<?> roomInfo(@PathVariable String roomId){
        return chatService.roomInfo(roomId);
    }
}
