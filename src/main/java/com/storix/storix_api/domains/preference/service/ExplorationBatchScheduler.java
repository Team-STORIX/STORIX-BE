package com.storix.storix_api.domains.preference.service;

import com.storix.storix_api.domains.preference.application.helper.ExplorationCacheHelper;
import com.storix.storix_api.domains.preference.domain.PreferenceExploration;
import com.storix.storix_api.domains.preference.dto.PendingSwipeDto;
import com.storix.storix_api.domains.preference.repository.ExplorationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExplorationBatchScheduler {

    private final ExplorationCacheHelper cacheHelper;
    private final ExplorationRepository explorationRepository;

    // 5분마다 실행
    @Scheduled(fixedDelay = 300000)
    @Transactional
    public void flushExplorationDataToDb() {

        int batchSize = 100;
        List<PendingSwipeDto> batch = cacheHelper.popBatchFromGlobalQueue(batchSize);

        if (batch.isEmpty()) {
            return;
        }

        List<PreferenceExploration> entities = batch.stream()
                .map(dto -> PreferenceExploration.builder()
                        .userId(dto.userId())
                        .worksId(dto.worksId())
                        .isLiked(dto.isLiked())
                        .build())
                .toList();

        try {
            explorationRepository.saveAll(entities);
            log.info("Flushed {} exploration records to DB.", entities.size());
        } catch (Exception e) {
            log.error("Failed to save exploration batch to DB", e);

            // 실패 시 데이터를 다시 글로벌 큐에 삽입 -> 다음 주기에 처리
            for (PendingSwipeDto dto : batch) {
                cacheHelper.rePushToGlobalQueue(dto);
            }
        }
    }
}