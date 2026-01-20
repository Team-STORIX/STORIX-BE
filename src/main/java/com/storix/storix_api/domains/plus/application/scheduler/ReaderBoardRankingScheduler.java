package com.storix.storix_api.domains.plus.application.scheduler;

import com.storix.storix_api.domains.plus.adaptor.BoardAdaptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReaderBoardRankingScheduler {

    private final BoardAdaptor boardAdaptor;

    @Scheduled(cron = "0 0 * * * *")
    public void calculateReaderBoardPopularity() {

        LocalDateTime threshold = LocalDateTime.now().minusDays(7);

        int updated =
                boardAdaptor.updateAllPopularityScoresRecentDays(threshold);

        log.info(">>>> [Scheduler] 총 {}개의 독자 게시물 인기 점수 갱신 완료.", updated);
    }
}