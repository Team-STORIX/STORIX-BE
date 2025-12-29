package com.storix.storix_api.domains.works.service;

import com.storix.storix_api.domains.search.dto.ArtistSearchResponseDto;
import com.storix.storix_api.domains.search.dto.IntegratedSearchResponseDto;
import com.storix.storix_api.domains.search.dto.WorksSearchResponseDto;
import com.storix.storix_api.domains.search.service.SearchHistoryService;
import com.storix.storix_api.domains.works.application.port.LoadWorksPort;
import com.storix.storix_api.domains.works.application.usecase.SearchWorksUseCase;
import com.storix.storix_api.domains.works.domain.Works;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorksSearchService implements SearchWorksUseCase {

    private final LoadWorksPort loadWorksPort;
    private final SearchHistoryService searchHistoryService;

    @Override
    @Transactional
    public IntegratedSearchResponseDto search(String keyword, int sortType, int page, int size, Long userId) {

        // 검색어 로그 저장 (Redis)
        searchHistoryService.addSearchLog(userId, keyword);

        // DB 작품 조회
        PageRequest pageRequest = PageRequest.of(page, size, createSort(sortType));
        Slice<Works> worksSlice = loadWorksPort.searchWorks(keyword, pageRequest);

        // DB 작가명 조회 (5)
        List<String> artistNames = loadWorksPort.searchArtistNames(keyword, 5);

        List<WorksSearchResponseDto> workDtos = worksSlice.getContent().stream()
                .map(this::toWorkDto)
                .toList();

        List<ArtistSearchResponseDto> artistDtos = artistNames.stream()
                .map(name -> ArtistSearchResponseDto.builder()
                        .artistName(name)
                        .build())
                .toList();

        return IntegratedSearchResponseDto.builder()
                .worksList(workDtos)
                .artistsList(artistDtos)
                .build();
    }

    // Works -> WorksSearchResponseDto 매핑
    private WorksSearchResponseDto toWorkDto(Works works) {
        return WorksSearchResponseDto.builder()
                .worksId(works.getId())
                .worksName(works.getWorksName())
                .artistName(works.getArtistName())
                .thumbnailUrl(works.getThumbnailUrl())
                .reviewsCount(works.getReviewsCount() != null ? works.getReviewsCount().longValue() : 0L)
                .avgRating(works.getAvgRating() != null ? works.getAvgRating() : 0.0)
                .worksType(works.getWorksType().name())
                .build();
    }

    private Sort createSort(int sortType) {
        return switch (sortType) {
            case 1 -> Sort.by(Sort.Direction.DESC, "avgRating");
            case 2 -> Sort.by(Sort.Direction.DESC, "reviewsCount");
            default -> Sort.by(Sort.Direction.DESC, "id");
        };
    }
}
