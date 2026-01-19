package com.storix.storix_api.domains.chat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.storix.storix_api.domains.chat.domain.ChatMessage;
import com.storix.storix_api.domains.chat.domain.MessageType;

import java.time.LocalDateTime;

public record ChatMessageResponseDto(
        Long roomId,
        Long senderId,
        String senderName,
        String message,
        MessageType messageType,
        @JsonFormat(
                shape = JsonFormat.Shape.STRING,
                pattern = "yyyy-MM-dd'T'HH:mm:ss",
                timezone = "Asia/Seoul")
        LocalDateTime createdAt
) {
    public static ChatMessageResponseDto from(ChatMessage entity) {
        return new ChatMessageResponseDto(
                entity.getRoomId(),
                entity.getSenderId(),
                entity.getSenderName(),
                entity.getMessage(),
                entity.getMessageType(),
                entity.getCreatedAt()
        );
    }
}
