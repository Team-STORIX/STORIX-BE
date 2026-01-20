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
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
public class StompHandler implements ChannelInterceptor {
    private final TokenProvider tokenProvider;
    private final RedisMessageListenerContainer container;
    private final RedisSubscriber subscriber;

    // roomId -> Topic 객체
    private static final Map<String, ChannelTopic> topics = new ConcurrentHashMap<>();
    // roomId -> 구독자 수
    private static final Map<String, AtomicInteger> roomSubscriberCounts = new ConcurrentHashMap<>();
    // SessionId -> { SubscriptionId -> RoomId }
    private static final Map<String, Map<String, String>> sessionSubscriptionMap = new ConcurrentHashMap<>();

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
            handleConnect(accessor);
        }

        // 구독 시점: Redis 리스너 등록
        if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            handleSubscribe(accessor);
        }

        // 리스너 정리 및 카운터 감소
        if (StompCommand.UNSUBSCRIBE.equals(accessor.getCommand())) {
            handleUnsubscribe(accessor);
        }

        // 세션 전체 해제
        if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {
            handleDisconnect(accessor);
        }

        return message;
    }

    private void handleConnect(StompHeaderAccessor accessor) {
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

    private void handleSubscribe(StompHeaderAccessor accessor) {
        String destination = accessor.getDestination();
        log.info(">>>> [STOMP] SUBSCRIBE 요청 감지: {}", destination);

        if (destination != null && destination.startsWith("/sub/chat/room/")) {
            String roomId = destination.substring(destination.lastIndexOf('/') + 1);
            String sessionId = accessor.getSessionId();
            String subId = accessor.getSubscriptionId();

            if (subId == null) {
                log.warn(">>>> [STOMP] Subscribe Failed: No Subscription ID");
                return;
            }

            // 세션-구독 매핑 저장 (SessionId -> SubId -> RoomId)
            sessionSubscriptionMap
                    .computeIfAbsent(sessionId, k -> new ConcurrentHashMap<>())
                    .put(subId, roomId);

            // 구독자 수 증가
            int currentCount = roomSubscriberCounts.computeIfAbsent(roomId, k -> new AtomicInteger(0)).incrementAndGet();

            // 구독자 수 증가 및 리스너 등록
            increaseCounter(roomId);
        }
    }

    // 특정 구독만 해제
    private void handleUnsubscribe(StompHeaderAccessor accessor) {
        String sessionId = accessor.getSessionId();
        String subId = accessor.getSubscriptionId();

        Map<String, String> subscriptions = sessionSubscriptionMap.get(sessionId);
        if (subscriptions != null && subId != null) {
            String roomId = subscriptions.remove(subId); // 해당 구독 정보만 제거
            if (roomId != null) {
                decreaseCounter(roomId); // 카운트 감소
                log.info(">>>> [STOMP] Unsubscribe: Session={}, Room={}", sessionId, roomId);
            }
        }
    }

    private void handleDisconnect(StompHeaderAccessor accessor) {
        String sessionId = accessor.getSessionId();

        // 세션의 모든 구독 정보 제거 및 반환
        Map<String, String> subscriptions = sessionSubscriptionMap.remove(sessionId);

        if (subscriptions != null) {
            for (String roomId : subscriptions.values()) {
                decreaseCounter(roomId); // 모든 구독 방에 대해 카운트 감소
            }
            log.info(">>>> [STOMP] Disconnect: Session={}, Cleaned up {} subscriptions", sessionId, subscriptions.size());
        }
    }

    // 카운트 증가
    private void increaseCounter(String roomId) {
        int currentCount = roomSubscriberCounts.computeIfAbsent(roomId, k -> new AtomicInteger(0)).incrementAndGet();
        if (currentCount == 1) {
            topics.computeIfAbsent(roomId, id -> {
                ChannelTopic topic = new ChannelTopic("room:" + id);
                container.addMessageListener(subscriber, topic);
                log.info(">>>> [Redis] New Listener Registered: room:{}", id);
                return topic;
            });
        }
    }

    // 카운트 감소
    private void decreaseCounter(String roomId) {
        AtomicInteger counter = roomSubscriberCounts.get(roomId);
        if (counter != null) {
            int remainingCount = counter.decrementAndGet();
            if (remainingCount <= 0) {
                ChannelTopic topic = topics.remove(roomId);
                if (topic != null) {
                    container.removeMessageListener(subscriber, topic);
                    log.info(">>>> [Redis] Listener Removed: room:{}", roomId);
                }
                roomSubscriberCounts.remove(roomId);
            }
        }
    }


    @PreDestroy
    public void cleanup() {
        log.info(">>>> [Cleanup] 채팅 서버 종료 시 리소스 정리 시작");
        try {
            topics.clear();
            roomSubscriberCounts.clear();
            sessionSubscriptionMap.clear();
            log.info(">>>> [Cleanup] 모든 채팅 리소스(로컬 캐시 데이터)가 정상적으로 해제되었습니다.");
        } catch (Exception e) {
            log.error(">>>> [Cleanup Error] 리소스 해제 중 오류 발생: ", e);
        }
    }
}