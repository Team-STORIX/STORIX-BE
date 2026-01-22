package com.storix.storix_api.domains.profile.application.service;

import com.storix.storix_api.domains.favorite.adaptor.FavoriteArtistAdaptor;
import com.storix.storix_api.domains.favorite.adaptor.FavoriteWorksAdaptor;
import com.storix.storix_api.domains.plus.adaptor.ReviewAdaptor;
import com.storix.storix_api.domains.plus.domain.Rating;
import com.storix.storix_api.domains.plus.dto.RatingCountInfo;
import com.storix.storix_api.domains.plus.dto.ReviewedWorksIdAndRatingInfo;
import com.storix.storix_api.domains.profile.dto.FavoriteWorksWithReviewInfo;
import com.storix.storix_api.domains.profile.dto.RatingCountResponse;
import com.storix.storix_api.domains.user.dto.FavoriteArtistInfo;
import com.storix.storix_api.domains.user.adaptor.UserAdaptor;
import com.storix.storix_api.domains.works.application.port.LoadWorksPort;
import com.storix.storix_api.domains.works.dto.WorksInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileFavoriteService {

    private final FavoriteArtistAdaptor favoriteArtistAdaptor;
    private final FavoriteWorksAdaptor favoriteWorksAdaptor;
    private final ReviewAdaptor reviewAdaptor;

    private final UserAdaptor userAdaptor;

    private final LoadWorksPort loadWorksPort;

    // 관심 작가 등록수 조회
    @Transactional(readOnly = true)
    public int findTotalFavoriteArtistCount(Long userId) {
        return favoriteArtistAdaptor.countFavoriteArtist(userId);
    }

    // 관심 작가 정보 조회
    @Transactional(readOnly = true)
    public Slice<FavoriteArtistInfo> findAllFavoriteArtistInfo(Long userId, Pageable pageable) {

        // 관심 작가 등록 리스트 조회
        Slice<Long> artistIdsSlice = favoriteArtistAdaptor.findAllFavoriteArtistsId(userId, pageable);
        List<Long> artistIds = artistIdsSlice.getContent();

        if (artistIds.isEmpty()) {
            return new SliceImpl<>(List.of(), pageable, artistIdsSlice.hasNext());
        }

        // 관심 작가 정보 조회
        List<FavoriteArtistInfo> artistList = userAdaptor.findAllFavoriteArtistInfoByArtistIds(artistIds);

        // 관심 작가 등록 순으로 정렬
        Map<Long, FavoriteArtistInfo> map = artistList.stream()
                .collect(Collectors.toMap(
                        FavoriteArtistInfo::artistId,
                        Function.identity(),
                        (existing, replacement) -> existing
                ));

        List<FavoriteArtistInfo> ordered = artistIds.stream()
                .map(map::get)
                .filter(Objects::nonNull)
                .toList();

        // 관심 작가 정보 리스트
        return new SliceImpl<>(ordered, pageable, artistIdsSlice.hasNext());
    }

    // 관심 작품 등록수 조회
    @Transactional(readOnly = true)
    public int findTotalFavoriteWorksCount(Long userId) {
        return favoriteWorksAdaptor.countFavoriteWorks(userId);
    }

    // 관심 작품 정보 조회
    @Transactional(readOnly = true)
    public Slice<FavoriteWorksWithReviewInfo> findAllFavoriteWorksInfo(Long userId, Pageable pageable) {

        // 관심 작품 등록 리스트 조회
        Slice<Long> worksIdsSlice = favoriteWorksAdaptor.findAllFavoriteWorksId(userId, pageable);
        List<Long> worksIds = worksIdsSlice.getContent();

        if (worksIds.isEmpty()) {
            return new SliceImpl<>(List.of(), pageable, worksIdsSlice.hasNext());
        }

        // 1) 관심 작품 정보 조회
        Map<Long, WorksInfo> worksMap =
                loadWorksPort.findAllWorksInfoByWorksIds(worksIds);

        // 2) 리뷰 관련 정보 조회
        List<ReviewedWorksIdAndRatingInfo> reviewedList =
                reviewAdaptor.findAllReviewInfoByFavoriteWorks(userId, worksIds);

        Map<Long, Rating> ratingMap = reviewedList.stream()
                .collect(Collectors.toMap(
                        ReviewedWorksIdAndRatingInfo::worksId,
                        ReviewedWorksIdAndRatingInfo::rating,
                        (existing, replacement) -> existing
                ));

        // 관심 작품 등록 순으로 1) 관심 작품 정보, 2) 리뷰 관련 정보 정렬
        List<FavoriteWorksWithReviewInfo> ordered = worksIds.stream()
                .map(worksId -> {
                    WorksInfo worksInfo = worksMap.get(worksId);
                    if (worksInfo == null) return null;

                    Rating ratingEnum = ratingMap.get(worksId);
                    boolean isReviewed = ratingEnum != null;
                    String rating = isReviewed ? ratingEnum.getDbValue() : null;

                    return FavoriteWorksWithReviewInfo.of(worksInfo, isReviewed, rating);
                })
                .filter(Objects::nonNull)
                .toList();

        // 관심 작품 정보 리스트
        return new SliceImpl<>(ordered, pageable, worksIdsSlice.hasNext());
    }

    // 별점 분포 조회
    @Transactional(readOnly = true)
    public RatingCountResponse findRatingDistributionByUserId(Long userId) {

        List<RatingCountInfo> raws = reviewAdaptor.countByRating(userId);

        Map<String, Long> result = Arrays.stream(Rating.values())
                .collect(Collectors.toMap(
                        Rating::getDbValue,
                        r -> 0L,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));

        for (RatingCountInfo dto : raws) {
            result.put(dto.rating().getDbValue(), dto.count());
        }

        return RatingCountResponse.of(result);
    }
}
