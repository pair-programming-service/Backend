package com.pair.website.configuration.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker // 메시지 브로커가 지원하는 'WebSocket 메시지 처리'를 활성화
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
        /*
        Endpoint를 지정
        */
        @Override
        public void registerStompEndpoints(StompEndpointRegistry registry){
            registry.addEndpoint("/ws/chat")
                    .setAllowedOrigins("*")
                    .withSockJS();
        }
        /*
        메모리 기반의 Simple Message Broker 활성화
        '/sub'으로 시작하는 주소의 subscriber들에게 메시지를 전달
        */
        @Override
        public void configureMessageBroker(MessageBrokerRegistry registry){
            // 해당 주소를 구독하고 있는 클라이언트들에게 메세지 전달 (1) 메세지 구독
            registry.enableSimpleBroker("/sub");
            // 클라이언트에서 보낸 메세지를 받을 prefix  (2) 메세지 발행 요청 : 메세지 수신
            registry.setApplicationDestinationPrefixes("/pub");

        }
}