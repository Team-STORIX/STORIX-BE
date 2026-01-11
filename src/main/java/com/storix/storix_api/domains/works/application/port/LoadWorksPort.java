package com.storix.storix_api.domains.works.application.port;

import com.storix.storix_api.domains.works.domain.Works;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface LoadWorksPort {

    Slice<Works> searchWorks(String keyword, Pageable pageable);

    void checkWorksExistById(Long worksId);
}
