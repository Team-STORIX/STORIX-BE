package com.storix.storix_api.global.websocket;

import com.storix.storix_api.domains.chat.service.RedisSubscriber;
import com.storix.storix_api.domains.user.adaptor.AuthUserDetails;
import com.storix.storix_api.domains.user.domain.Role;
import com.storix.storix_api.global.security.TokenProvider;
import com.storix.storix_api.global.security.dto.AccessTokenInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.messaging.*;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import jakarta.annotation.PreDestroy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class StompHandler implements ChannelInterceptor {
    private final TokenProvider tokenProvider;
    private final RedisMessageListenerContainer container;
    private final RedisSubscriber subscriber;

    private static final Map<String, ChannelTopic> topics = new ConcurrentHashMap<>();

    public StompHandler(TokenProvider tp, RedisMessageListenerContainer c, @Lazy RedisSubscriber s) {
        this.tokenProvider = tp;
        this.container = c;
        this.subscriber = s;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        // 연결 시점: JWT 토큰 인증
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String token = accessor.getFirstNativeHeader("Authorization");
            log.info(">>>> [STOMP] Connect Attempt - Token exists: {}", (token != null));

            if (token != null && token.startsWith("Bearer ")) {
                try {
                    AccessTokenInfo info = tokenProvider.parseAccessToken(token.substring(7));
                    AuthUserDetails user = new AuthUserDetails(info.userId(), Role.fromValue(info.role().replace("ROLE_", "")));

                    Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    accessor.setUser(auth);
                    log.info(">>>> [STOMP] 인증 성공: UserID {}", info.userId());
                } catch (Exception e) {
                    log.error(">>>> [STOMP] Auth Failed: {}", e.getMessage());
                    throw new MessageDeliveryException("UNAUTHORIZED");
                }
            } else {
                log.error(">>>> [STOMP] No Valid Token Found");
                throw new MessageDeliveryException("NO_TOKEN");
            }
        }

        // 구독 시점: Redis 리스너 등록
        if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            String destination = accessor.getDestination();
            log.info(">>>> [STOMP] SUBSCRIBE 요청 감지: {}", destination);

            if (destination != null && destination.startsWith("/sub/chat/room/")) {
                // 경로에서 roomId 추출
                String roomId = destination.substring(destination.lastIndexOf('/') + 1);

                // 해당 방에 대한 리스너가 없을 때만 새로 등록
                topics.computeIfAbsent(roomId, id -> {
                    ChannelTopic topic = new ChannelTopic("room:" + id);
                    container.addMessageListener(subscriber, topic);
                    log.info(">>>> [Redis] New Listener Registered for Topic: {}", topic.getTopic());
                    return topic;
                });
            }
        }

        return message;
    }

    @PreDestroy
    public void cleanup() {
        log.info(">>>> [Cleanup] 채팅 서버 종료 시 리소스 정리 시작");
        try {
            container.destroy();
            topics.clear();
            log.info(">>>> [Cleanup] 모든 채팅 리소스가 정상적으로 해제되었습니다.");
        } catch (Exception e) {
            log.error(">>>> [Cleanup Error] 리소스 해제 중 오류 발생: ", e);
        }
    }
}