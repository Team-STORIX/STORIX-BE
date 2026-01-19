package com.storix.storix_api.domains.chat.repository;

import com.storix.storix_api.domains.chat.domain.ChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepository extends JpaRepository<ChatMessage, Long> {

    // 특정 방의 메시지를 생성 시간순으로 조회
    Slice<ChatMessage> findAllByRoomId(Long roomId, Pageable pageable);
}