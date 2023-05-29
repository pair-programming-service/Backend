package com.pair.website.configuration.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {
    // preSend 메소드에서 클라이언트가 CONNECT할 때 헤더로 보낸 Authorization에 담긴 jwt Token을 검증하도록 한다. (jwt 검증)
    private final StompHandler stompHandler;

    // 소켓에 연결하기 위한 엔드 포인트를 지정해준다.
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(stompHandler,"ws/chat")
                // 실무에서는 정확한 도메인 지정
                .setAllowedOriginPatterns("http://localhost:8090");
    }
}