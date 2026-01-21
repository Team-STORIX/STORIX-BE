package com.storix.storix_api.domains.topicroom.service;

import com.storix.storix_api.domains.topicroom.application.port.LoadTopicRoomPort;
import com.storix.storix_api.domains.topicroom.application.port.LoadTopicRoomUserPort;
import com.storix.storix_api.domains.topicroom.dto.TopicRoomUserResponseDto;
import com.storix.storix_api.global.apiPayload.exception.topicRoom.UnknownTopicRoomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TopicRoomUserService {

    private final LoadTopicRoomUserPort loadTopicRoomUserPort;
    private final LoadTopicRoomPort loadTopicRoomPort;

    // 특정 토픽룸에 참여 중인 멤버들의 프로필 목록 조회
    @Transactional(readOnly = true)
    public List<TopicRoomUserResponseDto> getRoomMembers(Long roomId) {

        if (!loadTopicRoomPort.existsById(roomId)) {
            throw UnknownTopicRoomException.EXCEPTION;
        }

        return loadTopicRoomUserPort.loadMembersByRoomId(roomId);
    }
}