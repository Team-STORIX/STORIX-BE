package com.storix.storix_api.domains.works.adaptor;

import com.storix.storix_api.domains.works.dto.WorksInfo;
import com.storix.storix_api.domains.works.dto.LibraryWorksInfo;
import com.storix.storix_api.domains.works.domain.Works;
import com.storix.storix_api.domains.works.application.port.LoadWorksPort;
import com.storix.storix_api.domains.works.repository.WorksRepository;
import com.storix.storix_api.global.apiPayload.exception.plus.WorksNotExistException;
import com.storix.storix_api.global.apiPayload.exception.works.UnknownWorksException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class WorksPersistenceAdaptor implements LoadWorksPort {

    private final WorksRepository worksRepository;

    @Override
    public Slice<Works> searchWorks(String keyword, Pageable pageable) {
        return worksRepository.findBySearchKeyword(keyword, pageable);
    }

    @Override
    public Works findById(Long worksId) {
        return worksRepository.findById(worksId)
                .orElseThrow(() -> UnknownWorksException.EXCEPTION);
    }

    @Override
    public List<Long> findAllIdsByKeyword(String keyword) {
        return worksRepository.findAllIdsByKeyword(keyword);
    }

    @Override
    public Works findByIdWithHashtags(Long worksId) {
        return worksRepository.findByIdWithHashtags(worksId)
                .orElseThrow(() -> UnknownWorksException.EXCEPTION);
    }

    // 리뷰 도메인 용
    @Override
    public void checkWorksExistById(Long worksId) {
        if (!worksRepository.existsById(worksId)) {
            throw WorksNotExistException.EXCEPTION;
        }
    }

    @Override
    public Boolean isWorksForAdult(Long worksId) {
        return worksRepository.isWorksForAdult(worksId);
    }

    @Override
    public void updateIncrementingReviewInfoToWorks(Long worksId, double newRating) {
        worksRepository.incrementReviewsCountAndUpdateAverageRating(worksId, newRating);
    }

    @Override
    public void updateDecrementingReviewInfoToWorks(Long worksId, double newRating) {
        worksRepository.decrementReviewsCountAndUpdateAverageRating(worksId, newRating);
    }

    // 서재 도메인 용
    @Override
    public List<LibraryWorksInfo> getLibraryWorksInfo(List<Long> worksIds) {
        return worksRepository.findLibraryWorksInfoByIds(worksIds);
    }

    @Override
    public Slice<LibraryWorksInfo> searchLibraryWorksInfoByIds(List<Long> worksIds, String keyword, Pageable pageable) {
        return worksRepository.searchLibraryWorksInfoByIds(worksIds, keyword, pageable);
    }

    // 작품 정보 조회용
    @Override
    public Map<Long, WorksInfo> findAllWorksInfoByWorksIds(List<Long> worksIds) {
        if (worksIds == null || worksIds.isEmpty()) {
            return Collections.emptyMap();
        }

        List<WorksInfo> worksInfos = worksRepository.findWorksInfoByIds(worksIds);

        return worksInfos.stream()
                .collect(Collectors.toMap(
                        WorksInfo::worksId,
                        Function.identity()
                ));
    }

}
