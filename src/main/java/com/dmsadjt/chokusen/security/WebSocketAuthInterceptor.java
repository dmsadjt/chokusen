package com.dmsadjt.chokusen.security;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Component
public class WebSocketAuthInterceptor
    implements HandshakeInterceptor, ChannelInterceptor {

    private static final String USERNAME_ATTRIBUTE = "username";

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean beforeHandshake(
            @NonNull ServerHttpRequest request,
            @NonNull ServerHttpResponse response,
            @NonNull WebSocketHandler wsHandler,
            @NonNull Map<String, Object> attributes
    ) {
        if (!(request instanceof ServletServerHttpRequest servletRequest)) {
            return false;
        }
        HttpServletRequest httpRequest = servletRequest.getServletRequest();
        String token = JwtFilter.extractToken(httpRequest);
        if (token == null || !jwtUtil.isTokenValid(token)) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return false;
        }
        attributes.put(USERNAME_ATTRIBUTE, jwtUtil.extractUsername(token));
        return true;
    }

    @Override
    public void afterHandshake(
            @NonNull ServerHttpRequest request,
            @NonNull ServerHttpResponse response,
            @NonNull WebSocketHandler wsHandler,
            Exception exception
    ) {}

    @Override
    public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(
            message,
            StompHeaderAccessor.class
        );
        if (
            accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())
        ) {
            Map<String, Object> attributes = accessor.getSessionAttributes();
            Object username = attributes == null
                ? null
                : attributes.get(USERNAME_ATTRIBUTE);
            if (username == null) {
                throw new MessageDeliveryException("Unauthenticated");
            }
            accessor.setUser(
                new UsernamePasswordAuthenticationToken(username, null, List.of())
            );
        }
        return message;
    }
}
