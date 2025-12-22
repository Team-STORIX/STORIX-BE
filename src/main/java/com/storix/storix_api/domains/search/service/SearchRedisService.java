package com.storix.storix_api.domains.search.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SearchRedisService {

    private final RedisTemplate<String, String> redisTemplate;

    private static final String TRENDING_KEY = "search:trending";
    private static final String RECENT_KEY_PREFIX = "search:recent:";
    private static final int MAX_RECENT_SIZE = 10;


    /** 1. 검색어 저장 (인기 + 최근 검색어) */
    public void addSearchLog(Long userId, String keyword) {

        if (keyword == null || keyword.isBlank()) return;

        // 인기 검색어 점수 추가
        redisTemplate.opsForZSet().incrementScore(TRENDING_KEY, keyword, 1.0);

        // 로그인한 유저: 최근 검색어 저장
        if (userId != null) {

            String key = RECENT_KEY_PREFIX + userId;

            // 중복 제거 (이미 있으면 삭제 후 맨 앞으로 이동)
            redisTemplate.opsForList().remove(key, 1, keyword);
            redisTemplate.opsForList().leftPush(key, keyword);

            redisTemplate.opsForList().trim(key, 0, MAX_RECENT_SIZE - 1);
        }
    }

    /** 2. 급상승 검색어 조회 (Top 10) */
    public List<String> getTrendingKeywords() {

        Set<String> keywords = redisTemplate.opsForZSet().reverseRange(TRENDING_KEY, 0, 9);

        return keywords != null ? List.copyOf(keywords) : List.of();
    }

    /** 3. 최근 검색어 조회 */
    public List<String> getRecentKeywords(Long userId) {

        String key = RECENT_KEY_PREFIX + userId;
        List<String> keywords = redisTemplate.opsForList().range(key, 0, MAX_RECENT_SIZE - 1);

        return keywords != null ? keywords : List.of();
    }

    /** 4. 최근 검색어 삭제 */
    public void deleteRecentKeyword(Long userId, String keyword) {

        String key = RECENT_KEY_PREFIX + userId;

        redisTemplate.opsForList().remove(key, 1, keyword);
    }
}
