package com.storix.storix_api.search;

import com.storix.storix_api.domains.search.dto.SearchResponseWrapperDto;
import com.storix.storix_api.domains.search.dto.WorksSearchResponseDto;
import com.storix.storix_api.domains.search.service.SearchHistoryService;
import com.storix.storix_api.domains.search.service.SearchService;
import com.storix.storix_api.domains.works.application.port.LoadWorksPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SearchServiceTest {

    @InjectMocks
    private SearchService searchService;

    @Mock
    private LoadWorksPort loadWorksPort;
    @Mock
    private SearchHistoryService searchHistoryService;

    @Test
    @DisplayName("작품 검색 시 결과가 없으면 추천 검색어를 포함해 반환한다")
    void searchWorks_ReturnFallback_WhenEmpty() {

        // given
        String keyword = "없는작품";
        Pageable pageable = PageRequest.of(0, 10);
        String mockRecommendation = "추천작품";

        // 검색 결과가 비어있음을 가정
        given(loadWorksPort.searchWorks(anyString(), any(Pageable.class)))
                .willReturn(new SliceImpl<>(Collections.emptyList()));

        // 추천어 반환 가정
        given(searchHistoryService.getFallbackRecommendation()).willReturn(mockRecommendation);

        // when
        SearchResponseWrapperDto<WorksSearchResponseDto> response = searchService.searchWorks(1L, keyword, pageable);

        // then
        assertThat(response.getResult().getContent()).isEmpty();
        assertThat(response.getFallbackRecommendation()).isEqualTo(mockRecommendation);
        verify(searchHistoryService, times(1)).addSearchLog(anyLong(), anyString());
    }

    @Test
    @DisplayName("작품 검색 시 첫 페이지가 아니면 검색 로그를 저장하지 않는다")
    void searchWorks_NoLog_WhenNotFirstPage() {

        // given
        Pageable pageable = PageRequest.of(1, 10); // 2페이지
        given(loadWorksPort.searchWorks(anyString(), any(Pageable.class)))
                .willReturn(new SliceImpl<>(Collections.emptyList()));

        // when
        searchService.searchWorks(1L, "테스트", pageable);

        // then
        verify(searchHistoryService, never()).addSearchLog(anyLong(), anyString());
    }
}
