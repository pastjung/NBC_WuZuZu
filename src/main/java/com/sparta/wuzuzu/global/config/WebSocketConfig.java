package com.sparta.wuzuzu.global.config;

import com.sparta.wuzuzu.global.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtUtil jwtUtil;

    // 메세지 브로커 구성
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic/chat-rooms/"); // 구독 요청
        config.setApplicationDestinationPrefixes("/app"); // 접두사 처리
    }

    // STOMP 엔드포인트 등록
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/gs-guide-websocket"); // 핸드셰이크 엔드포인트 설정
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor headerAccessor = MessageHeaderAccessor.getAccessor(message,
                    StompHeaderAccessor.class);

                if (headerAccessor != null && headerAccessor.getCommand() == StompCommand.SEND) {
                    String token = headerAccessor.getFirstNativeHeader("Authorization");

                    if (token != null) {
                        token = token.substring(7);
                        jwtUtil.validateToken(token);
                    }
                }

                return message;
            }
        });
    }
}