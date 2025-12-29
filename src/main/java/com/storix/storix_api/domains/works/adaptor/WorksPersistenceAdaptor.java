package com.storix.storix_api.domains.works.adaptor;

import com.storix.storix_api.domains.works.domain.Works;
import com.storix.storix_api.domains.works.application.port.LoadWorksPort;
import com.storix.storix_api.domains.works.repository.WorksRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WorksPersistenceAdaptor implements LoadWorksPort {

    private final WorksRepository worksRepository;


    @Override
    public Slice<Works> searchWorks(String keyword, Pageable pageable) {

        return worksRepository.findByWorksNameContainingOrArtistNameContaining(keyword, keyword, pageable);
    }

    @Override
    public List<String> searchArtistNames(String keyword, int limit) {
        return worksRepository.findDistinctArtistNames(keyword, PageRequest.of(0, limit));
    }
}
