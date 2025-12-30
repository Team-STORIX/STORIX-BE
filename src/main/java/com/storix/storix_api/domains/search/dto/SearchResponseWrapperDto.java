package com.storix.storix_api.domains.search.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Slice;

@Getter
@Builder
public class SearchResponseWrapperDto<T> {

    private Slice<T> result;

    // 검색 결과가 없을 때 추천될 검색어
    private String fallbackRecommendation;
}
