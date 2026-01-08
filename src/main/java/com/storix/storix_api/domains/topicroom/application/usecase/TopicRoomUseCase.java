package com.storix.storix_api.domains.topicroom.application.usecase;

import com.storix.storix_api.domains.search.dto.SearchResponseWrapperDto;
import com.storix.storix_api.domains.topicroom.dto.TopicRoomCreateRequestDto;
import com.storix.storix_api.domains.topicroom.dto.TopicRoomReportRequestDto;
import com.storix.storix_api.domains.topicroom.dto.TopicRoomResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface TopicRoomUseCase {

    Slice<TopicRoomResponseDto> getMyJoinedRooms(Long userId, Pageable pageable);

    List<TopicRoomResponseDto> getTodayTrendingRooms();

    SearchResponseWrapperDto<TopicRoomResponseDto> searchRooms(String keyword, Pageable pageable);

    Long createRoom(Long userId, TopicRoomCreateRequestDto request);

    void joinRoom(Long userId, Long roomId);

    void leaveRoom(Long userId, Long roomId);

    void reportUser(Long reporterId, Long roomId, TopicRoomReportRequestDto request);

    void updateRoomLastChatTime(Long roomId);
}