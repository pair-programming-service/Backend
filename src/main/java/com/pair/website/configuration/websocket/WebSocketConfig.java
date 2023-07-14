package com.pair.website.configuration.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) { // Client에서 websocket연결할 때 사용할 API 경로를 설정
        registry.addEndpoint("/ws/chat") // 연결될 엔드포인트 // STOMP 접속 주소 url => /ws/chat (이 주소로 소켓을 연결)
                .setAllowedOriginPatterns("*") // CORS 설정 부분 // *으로 모든 도메인 허용함
                .withSockJS(); // 일부 소켓을 지원하지 않는 브라우저에서 SocketJS를 사용하게 함 // 이것때문에 연결이 안 되길래 삭제함
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 메시지를 구독하는 요청 url -> 메시지 받을 때
        registry.enableSimpleBroker("/sub");
        // 메시지를 발행하는 요청 url -> 메시지 보낼 때
        registry.setApplicationDestinationPrefixes("/pub");
    }
}