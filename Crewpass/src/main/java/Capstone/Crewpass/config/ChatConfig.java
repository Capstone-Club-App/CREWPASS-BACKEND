package Capstone.Crewpass.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class ChatConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) { // Client에서 websocket연결할 때 사용할 API 경로를 설정
        registry.addEndpoint("/ws/chat") // 연결될 엔드포인트 // STOMP 접속 주소 url => /ws/chat
                .setAllowedOriginPatterns("*")
                .withSockJS(); // SocketJS를 연결한다는 설정
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 메시지를 구독하는 요청 url -> 메시지 받을 때
        registry.enableSimpleBroker("/queue", "/topic");
        // 메시지를 발행하는 요청 url -> 메시지 보낼 때
        registry.setApplicationDestinationPrefixes("/app");
    }
}
