package com.storix.storix_api.domains.chat.application.usecase;

import com.storix.storix_api.domains.chat.dto.ChatMessageRequestDto;
import com.storix.storix_api.domains.chat.dto.ChatMessageResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface ChatUseCase {
    void sendMessage(Long userId, ChatMessageRequestDto request);
    Slice<ChatMessageResponseDto> getChatHistory(Long roomId, Pageable pageable);
}
