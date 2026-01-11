package com.storix.storix_api.domains.works.adaptor;

import com.storix.storix_api.domains.works.domain.Works;
import com.storix.storix_api.domains.works.application.port.LoadWorksPort;
import com.storix.storix_api.domains.works.repository.WorksRepository;
import com.storix.storix_api.global.apiPayload.exception.plus.WorksNotExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class WorksPersistenceAdaptor implements LoadWorksPort {

    private final WorksRepository worksRepository;

    @Override
    public Slice<Works> searchWorks(String keyword, Pageable pageable) {
        return worksRepository.findBySearchKeyword(keyword, pageable);
    }

    @Override
    public void checkWorksExistById(Long worksId) {
        if (!worksRepository.existsById(worksId)) {
            throw WorksNotExistException.EXCEPTION;
        }
    }
}
