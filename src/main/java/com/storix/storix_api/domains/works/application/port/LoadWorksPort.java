package com.storix.storix_api.domains.works.application.port;

import com.storix.storix_api.domains.works.domain.Works;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface LoadWorksPort {

    Slice<Works> searchWorks(String keyword, Pageable pageable);

    void checkWorksExistById(Long worksId);
  
    Works findById(Long worksId);

    // 키워드로 작품 ID 리스트만 조회 (검색용)
    List<Long> findAllIdsByKeyword(String keyword);

    Works findByIdWithHashtags(Long worksId);
}
