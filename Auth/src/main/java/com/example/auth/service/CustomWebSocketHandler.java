package com.example.auth.service;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class CustomWebSocketHandler implements WebSocketHandler {
    private final CopyOnWriteArrayList<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        try {
            sessions.add(session);
            System.out.println("Yeni WebSocket bağlantısı: " + session.getId());
        } catch (Exception e) {
            System.err.println("Bağlantı hatası: " + e.getMessage());
        }
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        if (message instanceof TextMessage textMessage) {
            String payload = textMessage.getPayload();
            System.out.println("Mesaj alındı: " + payload);
        }
        else if (message instanceof BinaryMessage binaryMessage) {
            ByteBuffer buffer = binaryMessage.getPayload();
            System.out.println("Binary mesaj alındı: " + buffer);
        }
        else if (message instanceof PongMessage pongMessage) {
            System.out.println("Pong mesajı alındı.");
        }
        else {
            System.err.println("Bilinmeyen mesaj türü: " + message.getClass().getName());
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        System.err.println("WebSocket Hatası: " + exception.getMessage());
        session.close(CloseStatus.SERVER_ERROR);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        sessions.remove(session);
        System.out.println("Bağlantı kapandı: " + session.getId());
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    public void broadcastMessage(String message) throws IOException {
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                session.sendMessage(new TextMessage(message));
            }
        }
    }
}
