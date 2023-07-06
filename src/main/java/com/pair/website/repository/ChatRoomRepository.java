package com.pair.website.repository;

import com.pair.website.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom,Long> {

    @Query(value = "select C from ChatRoom C where C.member1 = :name or C.member2 = :name")
    List<ChatRoom> findAllByRoom(@Param("name") String name);

    ChatRoom findByRoomId(String roomId);

}
