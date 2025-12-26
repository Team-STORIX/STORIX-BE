package com.storix.storix_api.domains.search.controller;

import com.storix.storix_api.domains.search.dto.IntegratedSearchResponseDto;
import com.storix.storix_api.domains.search.dto.RecentResponseDto;
import com.storix.storix_api.domains.search.dto.TrendingResponseDto;
import com.storix.storix_api.domains.search.service.SearchHistoryService;
import com.storix.storix_api.domains.user.adaptor.AuthUserDetails;
import com.storix.storix_api.domains.works.application.usecase.SearchWorksUseCase;
import com.storix.storix_api.global.apiPayload.CustomResponse;
import com.storix.storix_api.global.apiPayload.code.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
@Tag(name="검색", description = "검색 관련 API")
public class SearchController {

    private final SearchWorksUseCase searchWorkUseCase;
    private final SearchHistoryService searchHistoryService;

    @GetMapping
    @Operation(summary = "통합 검색", description = "작품 및 작가 검색 (무한스크롤)")
    public CustomResponse<IntegratedSearchResponseDto> search(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @AuthenticationPrincipal AuthUserDetails authUser
    ) {
        Long userId = (authUser != null) ? authUser.getUserId() : null;
        return CustomResponse.onSuccess(
                SuccessCode.SUCCESS,
                searchWorkUseCase.search(keyword, sort, page, size, userId)
        );
    }

    @GetMapping("/trending")
    @Operation(summary = "급상승 검색어 조회", description = "인기 검색어 1~10위와 순위 변동을 조회합니다.")
    public CustomResponse<TrendingResponseDto> getTrending() {
        return CustomResponse.onSuccess(
                SuccessCode.SUCCESS,
                TrendingResponseDto.builder()
                        .trendingKeywords(searchHistoryService.getTrendingKeywords())
                        .build()
        );
    }

    @GetMapping("/recent")
    @Operation(summary = "최근 검색어 조회")
    public CustomResponse<RecentResponseDto> getRecent(
            @AuthenticationPrincipal AuthUserDetails authUser
    ) {
        // authUser null 체크 필요시 추가
        return CustomResponse.onSuccess(
                SuccessCode.SUCCESS,
                RecentResponseDto.builder()
                        .recentKeywords(searchHistoryService.getRecentKeywords(authUser.getUserId()))
                        .build()
        );
    }

    @DeleteMapping("/recent")
    @Operation(summary = "최근 검색어 삭제")
    public CustomResponse<Void> deleteRecent(
            @RequestParam String keyword,
            @AuthenticationPrincipal AuthUserDetails authUser
    ) {
        searchHistoryService.deleteRecentKeyword(authUser.getUserId(), keyword);
        return CustomResponse.onSuccess(SuccessCode.SUCCESS, null);
    }
}
