package com.storix.storix_api.domains.chat.controller;

import com.storix.storix_api.domains.chat.domain.ChatMessage;
import com.storix.storix_api.domains.chat.domain.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;

    /**
     * [Message flow]
     * 1. 클라이언트가 "/pub/chat/message"로 메시지를 보냄
     * 2. 이 메서드가 받아서 처리
     * 3. "/sub/chat/room/{roomId}"를 구독 중인 사람들에게 메시지 전송
     */
    @MessageMapping("/chat/message")
    public void message(ChatMessage message) {

        // 추후 DB 저장 로직 추가 예정

        // 입장 메시지 처리
        if (MessageType.ENTER.equals(message.getMessageType())) {

            // senderName은 추후 토큰에서 가져오거나 클라이언트가 보내준 값 사용
            message = ChatMessage.builder()
                    .messageType(MessageType.ENTER)
                    .roomId(message.getRoomId())
                    .senderName("알림")
                    .message(message.getSenderName() + "님이 입장하셨습니다.")
                    .build();
        }

        // 해당 방(Topic)을 구독 중인 클라이언트들에게 메시지 발송
        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }

}
