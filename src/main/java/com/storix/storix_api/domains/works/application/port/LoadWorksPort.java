package com.storix.storix_api.domains.works.application.port;

import com.storix.storix_api.domains.works.dto.LibraryWorksInfo;
import com.storix.storix_api.domains.works.domain.Works;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface LoadWorksPort {

    Slice<Works> searchWorks(String keyword, Pageable pageable);
  
    Works findById(Long worksId);

    // 키워드로 작품 ID 리스트만 조회 (검색용)
    List<Long> findAllIdsByKeyword(String keyword);

    Works findByIdWithHashtags(Long worksId);

    // 리뷰 도메인 용
    void checkWorksExistById(Long worksId);

    boolean isWorksForAdult(Long worksId);

    void updateIncrementingReviewInfoToWorks(Long worksId, double newRating);

    void updateDecrementingReviewInfoToWorks(Long worksId, double newRating);

    // 서재 도메인 용
    List<LibraryWorksInfo> getLibraryWorksInfo(List<Long> worksIds);

    Slice<LibraryWorksInfo> searchLibraryWorksInfoByIds(List<Long> worksIds, String keyword, Pageable pageable);
}
