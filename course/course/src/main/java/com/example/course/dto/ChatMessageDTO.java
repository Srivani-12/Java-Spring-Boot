package com.example.course.dto;

import java.time.LocalDateTime;

public class ChatMessageDTO {

    private String message;

    private String senderId;

    private String receiverId;

    private LocalDateTime timestamp;

    public ChatMessageDTO(String message, String senderId, String receiverId, LocalDateTime timestamp) {
        this.message = message;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "ChatMessageDTO{" +
                "message='" + message + '\'' +
                ", senderId='" + senderId + '\'' +
                ", receiverId='" + receiverId + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
