package com.storix.storix_api.domains.works.dto;

import com.storix.storix_api.domains.hashtag.domain.Hashtag;
import com.storix.storix_api.domains.works.domain.Works;
import lombok.Builder;

import java.util.List;

@Builder
public record WorksDetailResponseDto(
        Long worksId,
        String worksName,
        String worksType,
        String thumbnailUrl,
        String author,
        String illustrator,
        String originalAuthor,
        String genre,
        String platform,
        String ageClassification,
        Double avgRating,
        String description,
        List<String> hashtags
) {
    public static WorksDetailResponseDto from(Works works) {
        return WorksDetailResponseDto.builder()
                .worksId(works.getId())
                .worksName(works.getWorksName())
                .worksType(works.getWorksType().getDbValue())
                .thumbnailUrl(works.getThumbnailUrl())
                .author(works.getAuthor())
                .illustrator(works.getIllustrator())
                .originalAuthor(works.getOriginalAuthor())
                .genre(works.getGenre().getDbValue())
                .platform(works.getPlatform().getDbValue())
                .ageClassification(works.getAgeClassification().getDbValue())
                .avgRating(works.getAvgRating())
                .description(works.getDescription())
                .hashtags(works.getHashtags().stream()
                        .map(Hashtag::getName)
                        .toList())
                .build();
    }
}
