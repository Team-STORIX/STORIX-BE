package com.storix.storix_api.domains.library.dto;

import com.storix.storix_api.domains.plus.domain.Rating;

public record LibraryWorksInfo(
        // 작품 정보
        Long worksId,
        String worksName,
        String artistName,
        String thumbnailUrl,
        String worksType,
        String genre,

        // 리뷰 정보
        Long reviewId,
        String rating
) {
    public static LibraryWorksInfo of(
            com.storix.storix_api.domains.works.dto.LibraryWorksInfo worksInfo,
            Long reviewId,
            Rating rating
    ) {
        return new LibraryWorksInfo(
                // 작품 정보
                worksInfo.worksId(),
                worksInfo.worksName(),
                worksInfo.artistName(),
                worksInfo.thumbnailUrl(),
                worksInfo.worksType().getDbValue(),
                worksInfo.genre().getDbValue(),

                // 리뷰 정보
                reviewId,
                rating.getDbValue()
        );
    }
}
