package com.storix.storix_api.domains.works.application.usecase;

import com.storix.storix_api.domains.search.dto.IntegratedSearchResponseDto;

public interface SearchWorksUseCase {

    IntegratedSearchResponseDto search(String keyword, int sort, int page, int size, Long userId);
}
