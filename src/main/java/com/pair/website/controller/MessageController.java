package com.pair.website.controller;
import com.amazonaws.services.kms.model.NotFoundException;
import com.pair.website.domain.ChatRoom;
import com.pair.website.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;


@Controller
@RequiredArgsConstructor
public class MessageController {
    private final ChatRoomRepository chatRoomRepository;

    @GetMapping("/")
    public String index(){
        return "error";
    }

    @GetMapping("/api/chat/{room_id}/{member_id}")
    public String chattingRoom(@PathVariable Long room_id, @PathVariable Long member_id,Model model){
        ChatRoom chatRoom = chatRoomRepository.findById(room_id).orElseThrow(
                () -> new NotFoundException("채팅방이 존재하지 않습니다.")
        );
        if(member_id.equals(chatRoom.getCustomer().getId()))
            model.addAttribute("name",chatRoom.getCustomer().getNickname());
        else if(member_id.equals(chatRoom.getSeller().getId()))
            model.addAttribute("name",chatRoom.getSeller().getNickname());
        else
            return "error";
        return "chattingRoom";
    }
}
