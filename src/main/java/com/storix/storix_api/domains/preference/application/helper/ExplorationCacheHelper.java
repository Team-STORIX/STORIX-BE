package com.storix.storix_api.domains.preference.application.helper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.storix.storix_api.domains.preference.dto.GenreScoreInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class ExplorationCacheHelper {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    private static final String CHART_KEY_PREFIX = "exploration::chart::total::";
    private static final String DONE_KEY_PREFIX = "exploration::done::today::";
    private static final String PENDING_LIST_PREFIX = "exploration::pending::detail::";
    private static final String GLOBAL_QUEUE_KEY = "exploration::queue";

    // 레이더 차트 분석용
    public List<GenreScoreInfo> getOrGenerateChart(Long userId, Supplier<List<GenreScoreInfo>> supplier) {

        String key = CHART_KEY_PREFIX + userId;
        String cached = redisTemplate.opsForValue().get(key);

        if (cached != null) {
            try {
                return objectMapper.readValue(cached, new TypeReference<>() {});
            } catch (Exception e) {
                redisTemplate.delete(key);
            }
        }

        // 캐시 없으면 DB 조회
        List<GenreScoreInfo> data = supplier.get();
        try {
            redisTemplate.opsForValue().set(key, objectMapper.writeValueAsString(data), Duration.ofDays(7));
        } catch (Exception ignored) {}

        return data;
    }


    public void addPendingSwipe(Long userId, Long worksId, boolean isLiked) {

        PendingSwipeDto dto = PendingSwipeDto.builder()
                .userId(userId).worksId(worksId).isLiked(isLiked).build();

        try {
            String json = objectMapper.writeValueAsString(dto);

            // 유저별 리스트에 저장 (하루 유지)
            redisTemplate.opsForList().rightPush(PENDING_LIST_PREFIX + userId, json);
            redisTemplate.expire(PENDING_LIST_PREFIX + userId, Duration.ofDays(1));

            // 스케줄러 사용을 위함
            redisTemplate.opsForList().rightPush(GLOBAL_QUEUE_KEY, json);

        } catch (Exception e) {
            log.warn(">>> 취향분석 cache helper [pending]: {}", String.valueOf(e));
        }
    }

    // 유저별 pending data 전체 조회
    public List<PendingSwipeDto> getAllPendingSwipes(Long userId) {
        List<String> rawList = redisTemplate.opsForList().range(PENDING_LIST_PREFIX + userId, 0, -1);
        if (rawList == null) return Collections.emptyList();

        return rawList.stream()
                .map(s -> {
                    try { return objectMapper.readValue(s, PendingSwipeDto.class); }
                    catch (Exception e) { return null; }
                })
                .filter(Objects::nonNull)
                .toList();
    }

    public Set<Long> getPendingWorksIds(Long userId) {
        return getAllPendingSwipes(userId).stream()
                .map(PendingSwipeDto::worksId)
                .collect(Collectors.toSet());
    }

    // 오늘 완료 마킹 및 캐시 관리
    public boolean isAlreadyParticipatedToday(Long userId) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(DONE_KEY_PREFIX + userId));
    }

    public void markAsParticipatedToday(Long userId) {
        Duration duration = Duration.between(LocalDateTime.now(), LocalDateTime.now().toLocalDate().atTime(LocalTime.MAX));
        redisTemplate.opsForValue().set(DONE_KEY_PREFIX + userId, "true", duration);
    }

    public void deleteChartCache(Long userId) {
        redisTemplate.delete(CHART_KEY_PREFIX + userId);
    }
}