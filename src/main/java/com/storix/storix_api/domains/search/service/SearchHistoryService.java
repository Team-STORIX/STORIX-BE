package com.storix.storix_api.domains.search.service;

import com.storix.storix_api.domains.search.dto.TrendingItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchHistoryService {

    private final RedisTemplate<String, String> redisTemplate;

    private static final String TRENDING_KEY = "search:trending";
    private static final String RECENT_KEY_PREFIX = "search:recent:";
    private static final String SNAPSHOT_KEY = "search:trending:snapshot";
    private static final int MAX_RECENT_SIZE = 10;
    private static final long RECENT_KEY_TTL_DAYS = 14;

    /** 1. 검색어 저장 (인기 + 최근 검색어) */
    public void addSearchLog(Long userId, String keyword) {

        if (keyword == null || keyword.isBlank()) return;

        // 인기 검색어 점수 추가
        redisTemplate.opsForZSet().incrementScore(TRENDING_KEY, keyword, 1.0);

        // 로그인한 유저: 최근 검색어 저장
        if (userId != null) {

            String key = RECENT_KEY_PREFIX + userId;

            redisTemplate.opsForList().remove(key, 1, keyword);
            redisTemplate.opsForList().leftPush(key, keyword);
            redisTemplate.opsForList().trim(key, 0, MAX_RECENT_SIZE - 1);

            // 최근 검색어 TTL 설정
            redisTemplate.expire(key, RECENT_KEY_TTL_DAYS, TimeUnit.DAYS);
        }
    }

    @Scheduled(fixedRate = 10 * 60 * 1000) // 10분 주기
    public void updateTrendingSnapshot() {
        // search:trending 내용 -> search:trending:snapshot에 덮어씀
        redisTemplate.opsForZSet().unionAndStore(TRENDING_KEY, List.of(TRENDING_KEY), SNAPSHOT_KEY);

        log.info("급상승 검색어 스냅샷 갱신 완료");
    }

    /** 2. 급상승 검색어 조회 (Top 10) */
    public List<TrendingItem> getTrendingKeywords() {

        // 현재 top 10
        Set<String> currentKeywords = redisTemplate.opsForZSet().reverseRange(TRENDING_KEY, 0, 9);

        if (currentKeywords == null || currentKeywords.isEmpty()) {
            return List.of();
        }

        List<TrendingItem> result = new ArrayList<>();
        int currentRank = 1;

        for (String keyword : currentKeywords) {
            // 과거 랭킹
            Long prevRankIndex = redisTemplate.opsForZSet().reverseRank(SNAPSHOT_KEY, keyword);

            String status = "SAME";

            if (prevRankIndex == null) {
                status = "NEW";
            } else {
                int prevRank = prevRankIndex.intValue() + 1;

                if (prevRank > currentRank) {
                    status = "UP";
                } else if (prevRank < currentRank) {
                    status = "DOWN";
                }
            }

            result.add(TrendingItem.builder()
                    .keyword(keyword)
                    .rank(currentRank)
                    .status(status)
                    .build());

            currentRank++;
        }

        return result;
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
