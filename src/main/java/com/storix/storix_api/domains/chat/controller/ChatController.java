package com.storix.storix_api.domains.chat.controller;

import com.storix.storix_api.domains.chat.application.usecase.ChatUseCase;
import com.storix.storix_api.domains.chat.domain.ChatMessage;
import com.storix.storix_api.domains.chat.domain.MessageType;
import com.storix.storix_api.domains.chat.dto.ChatMessageRequestDto;
import com.storix.storix_api.domains.chat.dto.ChatMessageResponseDto;
import com.storix.storix_api.domains.topicroom.application.usecase.TopicRoomUseCase;
import com.storix.storix_api.domains.user.adaptor.AuthUserDetails;
import com.storix.storix_api.domains.user.application.port.LoadUserPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatUseCase chatUseCase;

    @MessageMapping("/chat/message")
    public void message(@Payload ChatMessageRequestDto request, SimpMessageHeaderAccessor accessor) {
        Authentication auth = (Authentication) accessor.getUser();

        if (auth == null) {
            log.error(">>>> [채팅 에러] 인증되지 않은 세션입니다.");
            return;
        }

        AuthUserDetails user = (AuthUserDetails) auth.getPrincipal();
        chatUseCase.sendMessage(user.getUserId(), request);
    }
}