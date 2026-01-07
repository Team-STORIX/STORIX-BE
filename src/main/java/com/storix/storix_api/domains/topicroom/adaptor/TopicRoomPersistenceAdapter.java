package com.storix.storix_api.domains.topicroom.adaptor;

import com.storix.storix_api.domains.topicroom.application.port.LoadTopicRoomPort;
import com.storix.storix_api.domains.topicroom.application.port.RecordTopicRoomPort;
import com.storix.storix_api.domains.topicroom.domain.TopicRoom;
import com.storix.storix_api.domains.topicroom.domain.TopicRoomReport;
import com.storix.storix_api.domains.topicroom.domain.TopicRoomUser;
import com.storix.storix_api.domains.topicroom.domain.enums.TopicRoomRole;
import com.storix.storix_api.domains.topicroom.repository.TopicRoomReportRepository;
import com.storix.storix_api.domains.topicroom.repository.TopicRoomRepository;
import com.storix.storix_api.domains.topicroom.repository.TopicRoomUserRepository;
import com.storix.storix_api.global.apiPayload.exception.topicRoom.UnknownTopicRoomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TopicRoomPersistenceAdapter implements LoadTopicRoomPort, RecordTopicRoomPort {

    private final TopicRoomRepository topicRoomRepository;
    private final TopicRoomUserRepository topicRoomUserRepository;
    private final TopicRoomReportRepository topicRoomReportRepository;

    @Override public TopicRoom findById(Long roomId) {

        return topicRoomRepository.findById(roomId)
                .orElseThrow(() -> UnknownTopicRoomException.EXCEPTION);
    }

    @Override public Slice<TopicRoomUser> findParticipationsByUserId(Long userId, Pageable pageable) {

        return topicRoomUserRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }

    @Override public List<TopicRoom> findTop3Trending() {

        return topicRoomRepository.findTop3ByOrderByActiveUserNumberDesc();
    }

    @Override public Slice<TopicRoom> searchByWorksIdsOrDescription(List<Long> worksIds, String keyword, Pageable pageable) {

        return topicRoomRepository.findBySearchCondition(worksIds, keyword, pageable);
    }

    @Override public long countJoinedRooms(Long userId) {

        return topicRoomUserRepository.countByUserId(userId);
    }

    @Override public boolean existsByUserIdAndRoomId(Long userId, Long roomId) {

        return topicRoomUserRepository.existsByUserIdAndTopicRoomId(userId, roomId);
    }

    @Override public LocalDateTime getLastMessageTime(Long roomId) {
        // TODO: 채팅 서버 연동 시 구현
        return LocalDateTime.now().minusMinutes(5);
    }

    @Override public TopicRoom saveRoom(TopicRoom room) {

        return topicRoomRepository.save(room);
    }

    @Override
    public void saveParticipation(Long userId, TopicRoom room, TopicRoomRole role) {

        topicRoomUserRepository.save(new TopicRoomUser(room, userId, role));
    }

    @Override public void deleteParticipation(Long userId, Long roomId) {

        topicRoomUserRepository.deleteByUserIdAndTopicRoomId(userId, roomId);
    }

    @Override public void saveReport(TopicRoomReport report) {
        topicRoomReportRepository.save(report);
    }
}