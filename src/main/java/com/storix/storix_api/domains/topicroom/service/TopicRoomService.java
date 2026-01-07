package com.storix.storix_api.domains.topicroom.service;

import com.storix.storix_api.domains.search.dto.SearchResponseWrapperDto;
import com.storix.storix_api.domains.search.dto.TrendingItem;
import com.storix.storix_api.domains.search.service.SearchHistoryService;
import com.storix.storix_api.domains.topicroom.application.port.LoadTopicRoomPort;
import com.storix.storix_api.domains.topicroom.application.port.RecordTopicRoomPort;
import com.storix.storix_api.domains.topicroom.application.usecase.TopicRoomUseCase;
import com.storix.storix_api.domains.topicroom.domain.TopicRoom;
import com.storix.storix_api.domains.topicroom.domain.TopicRoomReport;
import com.storix.storix_api.domains.topicroom.domain.enums.TopicRoomRole;
import com.storix.storix_api.domains.topicroom.dto.TopicRoomCreateRequestDto;
import com.storix.storix_api.domains.topicroom.dto.TopicRoomReportRequestDto;
import com.storix.storix_api.domains.topicroom.dto.TopicRoomResponseDto;
import com.storix.storix_api.domains.user.application.port.LoadUserPort;
import com.storix.storix_api.domains.user.domain.User;
import com.storix.storix_api.domains.works.application.port.LoadWorksPort;
import com.storix.storix_api.domains.works.domain.Works;
import com.storix.storix_api.global.apiPayload.exception.topicRoom.MaxLimitException;
import com.storix.storix_api.global.apiPayload.exception.topicRoom.UnverifiedException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TopicRoomService implements TopicRoomUseCase {

    private final LoadTopicRoomPort loadTopicRoomPort;
    private final RecordTopicRoomPort recordTopicRoomPort;
    private final LoadUserPort loadUserPort;
    private final LoadWorksPort loadWorksPort;
    private final SearchHistoryService searchHistoryService;

    @Override
    public Slice<TopicRoomResponseDto> getMyJoinedRooms(Long userId, Pageable pageable) {

        return loadTopicRoomPort.findParticipationsByUserId(userId, pageable)
                .map(participation -> toDto(participation.getTopicRoom(), true));
    }

    @Override
    public List<TopicRoomResponseDto> getTodayTrendingRooms(Long userId) {

        // 현재 시간으로부터 24시간 전 시점 계산
        LocalDateTime twentyFourHoursAgo = LocalDateTime.now().minusHours(24);

        // 24시간 이내 생성된 방 중 인원순 Top 3 조회
        return loadTopicRoomPort.findTop3Trending(twentyFourHoursAgo).stream()
                .map(room -> {
                    boolean isJoined = userId != null && loadTopicRoomPort.existsByUserIdAndRoomId(userId, room.getId());
                    return toDto(room, isJoined);
                })
                .toList();
    }

    @Override
    public SearchResponseWrapperDto<TopicRoomResponseDto> searchRooms(String keyword, Pageable pageable) {
        // [Facade] Works 도메인에서 ID 조회 -> TopicRoom 조회
        List<Long> worksIds = loadWorksPort.findAllIdsByKeyword(keyword);
        Slice<TopicRoom> rooms = loadTopicRoomPort.searchByWorksIdsOrDescription(worksIds, keyword, pageable);

        String fallback = null;

        if (rooms.isEmpty()) {

            List<TrendingItem> trending = searchHistoryService.getTrendingKeywords();

            if (!trending.isEmpty()) {
                Collections.shuffle(trending);
                fallback = trending.get(0).getKeyword();
            }
        }

        return SearchResponseWrapperDto.<TopicRoomResponseDto>builder()
                .result(rooms.map(room -> toDto(room, false)))
                .fallbackRecommendation(fallback)
                .build();
    }

    @Override
    @Transactional
    public Long createRoom(Long userId, TopicRoomCreateRequestDto request) {

        User user = loadUserPort.findById(userId);
        Works works = loadWorksPort.findById(request.getWorksId());

        TopicRoom room = TopicRoom.builder()
                .topicRoomName(request.getTopicRoomName())
                .worksId(works.getId())
                .build();

        TopicRoom savedRoom = recordTopicRoomPort.saveRoom(room);
        recordTopicRoomPort.saveParticipation(user.getId(), savedRoom, TopicRoomRole.HOST);
        savedRoom.addParticipant();

        return savedRoom.getId();
    }

    @Override
    @Transactional
    public void joinRoom(Long userId, Long roomId) {

        // [Facade] User + TopicRoom + Works 정보 조합하여 입장 규칙 검사
        User user = loadUserPort.findById(userId);
        TopicRoom room = loadTopicRoomPort.findById(roomId);
        Works works = loadWorksPort.findById(room.getWorksId());

        if (!user.getIsAdultVerified() && "18세 이용가".equals(works.getAgeClassification())) {

            throw UnverifiedException.EXCEPTION;
        }

        if (loadTopicRoomPort.countJoinedRooms(userId) >= 9) {

            throw MaxLimitException.EXCEPTION;
        }

        if (!loadTopicRoomPort.existsByUserIdAndRoomId(userId, roomId)) {
            recordTopicRoomPort.saveParticipation(userId, room, TopicRoomRole.MEMBER);
            room.addParticipant();
        }
    }

    @Override
    @Transactional
    public void leaveRoom(Long userId, Long roomId) {
        TopicRoom room = loadTopicRoomPort.findById(roomId);
        recordTopicRoomPort.deleteParticipation(userId, roomId);
        room.removeParticipant();
    }

    @Override
    @Transactional
    public void reportUser(Long reporterId, Long roomId, TopicRoomReportRequestDto request) {

        TopicRoomReport report = TopicRoomReport.builder()
                .reporterId(reporterId)
                .reportedUserId(request.getReportedUserId())
                .topicRoomId(roomId)
                .reason(request.getReason())
                .otherReason(request.getOtherReason())
                .build();
        recordTopicRoomPort.saveReport(report);
    }

    // [Facade] 여러 도메인 정보를 조합해 DTO 생성
    private TopicRoomResponseDto toDto(TopicRoom room, boolean isJoined) {

        Works works = loadWorksPort.findById(room.getWorksId());
        LocalDateTime lastChat = loadTopicRoomPort.getLastMessageTime(room.getId());

        return TopicRoomResponseDto.builder()
                .topicRoomId(room.getId())
                .topicRoomName(room.getTopicRoomName())
                .worksType(works.getWorksType().getDbValue())
                .worksName(works.getWorksName())
                .thumbnailUrl(works.getThumbnailUrl())
                .activeUserNumber(room.getActiveUserNumber())
                .lastChatTime(formatTimeAgo(lastChat))
                .isJoined(isJoined)
                .build();
    }

    private String formatTimeAgo(LocalDateTime time) {

        if (time == null) return "대화 없음";

        long diff = Duration.between(time, LocalDateTime.now()).toMinutes();

        if (diff < 1) return "방금 전";
        if (diff < 60) return diff + "분 전";
        if (diff < 1440) return (diff / 60) + "시간 전";

        return (diff / 1440) + "일 전";
    }
}