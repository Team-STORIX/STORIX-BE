package com.storix.storix_api.domains.search.dto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WorksSearchResponseDto {

    private Long worksId;
    private String worksName;
    private String artistName;
    private int reviewsCount;
    private double avgRating;
    private String thumbnailUrl;
}
