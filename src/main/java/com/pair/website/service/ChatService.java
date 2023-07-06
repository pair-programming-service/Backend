package com.pair.website.service;

import com.pair.website.domain.ChatRoom;
import com.pair.website.domain.Member;
import com.pair.website.dto.response.BaseResponseDto;
import com.pair.website.dto.response.ChatRoomResponseDto;
import com.pair.website.repository.ChatRepository;
import com.pair.website.repository.ChatRoomRepository;
import com.pair.website.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;

    /*
     * 채팅방 생성
     * */
    @Transactional
    public BaseResponseDto<?> createRoom(String nickname, Member member) {
        String roomId = "";
        while(true) {
            roomId = UUID.randomUUID().toString();
            if(roomId.equals(chatRoomRepository.findByRoomId(roomId)))
                continue;
            else
                break;
        }
        ChatRoom room = ChatRoom.builder()
                .roomId(roomId)
                .member1(nickname)
                .member2(member.getNickname())
                .build();
        chatRoomRepository.save(room);

        ChatRoomResponseDto chatRoom = ChatRoomResponseDto.builder()
                .roomId(roomId)
                .member1(room.getMember1())
                .member2(room.getMember2())
                .build();
        return BaseResponseDto.success(chatRoom);
    }

    /*
     * 유저 채팅방 조회
     * */
    @Transactional(readOnly = true)
    public BaseResponseDto<?> findRoom(Member member) {
        List<ChatRoom> chatRoomList = chatRoomRepository.findAllByRoom(member.getNickname());

        return BaseResponseDto.success(chatRoomList);
    }

    /*
    * 채팅방 상세
    * */
    @Transactional(readOnly = true)
    public BaseResponseDto<?> roomInfo(String roomId) {
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomId);
        return BaseResponseDto.success(chatRoom);
    }
}
