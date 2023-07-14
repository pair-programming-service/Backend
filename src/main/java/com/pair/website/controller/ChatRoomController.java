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
@RequestMapping("/api")
public class ChatRoomController {
    private final ChatService chatService;
    private final PublicMethod publicMethod;

    /*
    * 채팅방 생성
    * */
    @PostMapping("/room/{nickname}")
    public BaseResponseDto<?> create(@PathVariable String nickname, HttpServletRequest request){
        BaseResponseDto<?> result = publicMethod.checkLogin(request);
        if (!result.isSuccess()) return result;
        Member member = (Member) result.getData();
        return chatService.createRoom(nickname,member);
    }

    /*
    * 유저 채팅방 조회
    * */
    @GetMapping("/room")
    public BaseResponseDto<?> findRoom(HttpServletRequest request){
        BaseResponseDto<?> result = publicMethod.checkLogin(request);
        if (!result.isSuccess()) return result;
        Member member = (Member) result.getData();
        return chatService.findRoom(member);
    }

    /*
    * 채팅방 상세 ( 이전 대화 목록 )
    * */
    @GetMapping("/room/{roomId}")
    public BaseResponseDto<?> roomInfo(@PathVariable String roomId){
        return chatService.roomInfo(roomId);
    }
}
