package com.storix.storix_api.domains.chat.adaptor;

import com.storix.storix_api.domains.chat.application.port.PublishChatPort;
import com.storix.storix_api.domains.chat.dto.ChatMessageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisChatAdapter implements PublishChatPort {

    private final RedisTemplate<String, Object> jsonRedisTemplate;

    public RedisChatAdapter(@Qualifier("jsonRedisTemplate") RedisTemplate<String, Object> jsonRedisTemplate) {
        this.jsonRedisTemplate = jsonRedisTemplate;
    }

    @Override
    public void publish(ChatMessageResponseDto response) {

        // "room:1" 형식으로 메시지 전송
        jsonRedisTemplate.convertAndSend("room:" + response.roomId(), response);
    }
}