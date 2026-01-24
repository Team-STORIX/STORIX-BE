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
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private static final String KEY_PREFIX = "exploration::chart::v1::";
    private static final String PARTICIPATION_KEY_PREFIX = "exploration::done::today::";

    public List<GenreScoreInfo> getOrGenerateChart(Long userId, Supplier<List<GenreScoreInfo>> scoreSupplier) {
        String key = KEY_PREFIX + userId;
        Object cached = redisTemplate.opsForValue().get(key);

        if (cached != null) {
            try {
                return objectMapper.readValue((String) cached, new TypeReference<>() {});
            } catch (Exception e) { redisTemplate.delete(key); }
        }

        List<GenreScoreInfo> scores = scoreSupplier.get();
        try {
            redisTemplate.opsForValue().set(key, objectMapper.writeValueAsString(scores), Duration.ofDays(7));
        } catch (Exception ignored) {}

        return scores;
    }

    public void deleteChartCache(Long userId) {
        redisTemplate.delete(KEY_PREFIX + userId);
    }

    public boolean isAlreadyParticipatedToday(Long userId) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(PARTICIPATION_KEY_PREFIX + userId));
    }

    public void markAsParticipatedToday(Long userId) {
        String key = PARTICIPATION_KEY_PREFIX + userId;
        // 오늘 남은 시간 계산 (자정 만료)
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime midnight = now.toLocalDate().atTime(LocalTime.MAX);
        Duration duration = Duration.between(now, midnight);

        redisTemplate.opsForValue().set(key, "true", duration);
    }
}