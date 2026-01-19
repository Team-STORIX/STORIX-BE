package com.storix.storix_api.domains.topicroom.service;

import com.storix.storix_api.domains.topicroom.domain.TopicRoom;
import com.storix.storix_api.domains.topicroom.repository.TopicRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class TopicRoomRankingScheduler {

    private final TopicRoomRepository topicRoomRepository;

    @Scheduled(fixedRate = 600000) // 10분마다 실행
    @Transactional
    public void calculatePopularity() {
        log.info(">>>> [Scheduler] 인기도 점수 계산 시작");
        List<TopicRoom> rooms = topicRoomRepository.findAll();
        LocalDateTime now = LocalDateTime.now();

        for (TopicRoom room : rooms) {
            double score = calculateScore(room, now);
            topicRoomRepository.updatePopularityScore(room.getId(), score);
        }
    }

    private double calculateScore(TopicRoom room, LocalDateTime now) {
        int u = room.getActiveUserNumber();
        LocalDateTime lastChat = room.getLastChatTime() != null ? room.getLastChatTime() : room.getCreatedAt();

        long hours = ChronoUnit.HOURS.between(lastChat, now);
        if (hours < 0) hours = 0;

        return (u * 10.0) / Math.pow((hours + 1), 1.8);
    }
}
