package com.storix.storix_api.domains.chat.service;

import com.storix.storix_api.domains.chat.application.port.RecordChatPort;
import com.storix.storix_api.domains.chat.domain.ChatMessage;
import com.storix.storix_api.domains.topicroom.application.port.UpdateTopicRoomPort;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ChatAsyncService {

    private final RecordChatPort recordChatPort;
    private final UpdateTopicRoomPort updateTopicRoomPort;

    @Async("chatAsyncExecutor")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void processAfterMessageSent(ChatMessage chatMessage) {
        // 메시지 저장
        recordChatPort.saveMessage(chatMessage);
        // 토픽룸의 마지막 채팅 시간 갱신
        updateTopicRoomPort.updateLastChatTime(chatMessage.getRoomId(), chatMessage.getCreatedAt());
    }
}