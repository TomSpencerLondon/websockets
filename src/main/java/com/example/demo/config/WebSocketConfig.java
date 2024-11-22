package com.example.demo.config;

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
        // Enable a simple in-memory message broker for proposals and votes
        config.enableSimpleBroker("/topic/proposals", "/topic/votes"); // Separate destinations for proposals and votes
        config.setApplicationDestinationPrefixes("/app"); // Prefix for messages sent from the client to the server
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Register the WebSocket endpoint for proposals
        registry.addEndpoint("/proposal")
                .withSockJS(); // WebSocket for proposals
    }
}
