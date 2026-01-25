package com.dailycodework.dreamshops.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Kích hoạt một message broker đơn giản để gửi tin nhắn đến các đích bắt đầu bằng /topic và /queue
        config.enableSimpleBroker("/topic", "/queue");
        // Nhận tin nhắn gửi đến các đích bắt đầu bằng /app
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }
    
    //    client sẽ lắng nghe ntn
    //    const socket = new SockJS('http://localhost:8080/ws'); // CONNECT
    //    const stompClient = Stomp.over(socket);
    //    stompClient.connect({}, () => {
    //        stompClient.send('/app/hello', {}, JSON.stringify({ name: 'Quyet Anh' }));
    //        stompClient.subscribe('/topic/greetings', msg => {
    //            console.log(msg.body);
    //        });
    //    });
}
