package com.storix.storix_api.domains.library.application.service;

import com.storix.storix_api.domains.library.adaptor.LibraryAdaptor;
import com.storix.storix_api.domains.library.dto.LibraryWorksInfo;
import com.storix.storix_api.domains.plus.adaptor.ReviewAdaptor;
import com.storix.storix_api.domains.plus.dto.ReviewedWorksIdAndRatingInfo;
import com.storix.storix_api.domains.works.adaptor.WorksPersistenceAdaptor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LibraryService {

    private final LibraryAdaptor libraryAdaptor;
    private final ReviewAdaptor reviewAdaptor;
    private final WorksPersistenceAdaptor worksPersistenceAdaptor;

    // 총 리뷰 개수 정보 조회
    @Transactional(readOnly = true)
    public int getTotalReviewCount(Long userId) {
        return libraryAdaptor.findReviewCount(userId);
    }

    // 서재 내 리뷰한 작품 정보 조회
    @Transactional(readOnly = true)
    public Slice<LibraryWorksInfo> getReviewedWorksInfo(Long userId, Pageable pageable) {

        // 리뷰 정보 조회
        Slice<ReviewedWorksIdAndRatingInfo> reviewInfo = reviewAdaptor.getWorksListByUserId(userId, pageable);

        // 작품 정보 조회
        List<Long> worksIds = reviewInfo.stream()
                .map(ReviewedWorksIdAndRatingInfo::worksId)
                .toList();

        if (worksIds.isEmpty()) {
            return new SliceImpl<>(List.of(), pageable, reviewInfo.hasNext());
        }

        List<com.storix.storix_api.domains.works.dto.LibraryWorksInfo> worksList = worksPersistenceAdaptor.getLibraryWorksInfo(worksIds);

        // 리뷰 정보 순서대로 세팅
        Map<Long, com.storix.storix_api.domains.works.dto.LibraryWorksInfo> worksMap = worksList.stream()
                .collect(Collectors.toMap(
                        com.storix.storix_api.domains.works.dto.LibraryWorksInfo::worksId,
                        Function.identity()));

        List<LibraryWorksInfo> content = reviewInfo.stream()
                .map(r -> LibraryWorksInfo.of(
                        worksMap.get(r.worksId()),
                        r.reviewId(),
                        r.rating()))
                .toList();

        return new SliceImpl<>(content, pageable, reviewInfo.hasNext());
    }

    // 서재 내 리뷰한 작품 정보 검색
    @Transactional(readOnly = true)
    public Slice<LibraryWorksInfo> searchReviewedWorksInfo(Long userId, String keyword, Pageable pageable) {

        // 모든 리뷰의 worksId, rating 리스트 조회
        List<ReviewedWorksIdAndRatingInfo> reviewInfo = reviewAdaptor.findAllWorksIdsByUserId(userId);

        // 리뷰한 작품 정보 검색
        List<Long> allWorksIds = reviewInfo.stream()
                .map(ReviewedWorksIdAndRatingInfo::worksId)
                .toList();

        if (allWorksIds.isEmpty()) {
            return new SliceImpl<>(List.of(), pageable, false);
        }

        Slice<com.storix.storix_api.domains.works.dto.LibraryWorksInfo> worksSlice = worksPersistenceAdaptor.searchLibraryWorksInfoByIds(allWorksIds, keyword, pageable);

        // 리뷰 정보 반영한 작품 검색 결과 세팅
        Map<Long, ReviewedWorksIdAndRatingInfo> reviewMap = reviewInfo.stream()
                .collect(Collectors.toMap(ReviewedWorksIdAndRatingInfo::worksId, Function.identity()));

        List<LibraryWorksInfo> content = worksSlice.getContent().stream()
                .map(w -> {
                    ReviewedWorksIdAndRatingInfo r = reviewMap.get(w.worksId());
                    if (r == null) return null;

                    return LibraryWorksInfo.of(w, r.reviewId(), r.rating());
                })
                .filter(Objects::nonNull)
                .toList();

        return new SliceImpl<>(content, pageable, worksSlice.hasNext());
    }

}
