package com.storix.storix_api.domains.chat.application.port;

import com.storix.storix_api.domains.chat.domain.ChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface LoadChatPort {
    Slice<ChatMessage> loadMessages(Long roomId, Pageable pageable);
}
