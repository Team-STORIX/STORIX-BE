package com.storix.storix_api.search;

import com.storix.storix_api.domains.search.service.SearchHistoryService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class SearchHistoryServiceTest {

    @InjectMocks
    private SearchHistoryService searchHistoryService;

    @Mock
    private RedisTemplate<String, String> redisTemplate;
    @Mock
    private ZSetOperations<String, String> zSetOperations;

    @Test
    @DisplayName("급상승 11-20위 검색어 중 랜덤하게 하나를 추천한다")
    void getFallbackRecommendation_Success() {

        // given
        Set<String> mockRankings = Set.of("검색어11", "검색어12", "검색어13");
        given(redisTemplate.opsForZSet()).willReturn(zSetOperations);
        given(zSetOperations.reverseRange(anyString(), eq(10L), eq(19L))).willReturn(mockRankings);

        // when
        String result = searchHistoryService.getFallbackRecommendation();

        // then
        assertThat(result).isIn(mockRankings);
    }
}
