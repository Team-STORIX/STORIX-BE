package com.storix.storix_api.domains.topicroom.application.port;

import java.util.List;
import java.util.Set;

public interface LoadTopicRoomUserPort {
    // 특정 유저의 전체 토픽룸 입장 정보
    Set<Long> loadJoinedRoomIds(Long userId, List<Long> roomIds);

    // 특정 유저가 특정 방에 참여 중인지 확인
    boolean existsByUserIdAndRoomId(Long userId, Long roomId);
}
