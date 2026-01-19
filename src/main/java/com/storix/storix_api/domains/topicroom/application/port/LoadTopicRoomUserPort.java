package com.storix.storix_api.domains.topicroom.application.port;

import java.util.List;
import java.util.Set;

public interface LoadTopicRoomUserPort {
    // 특정 유저의 전체 토픽룸 입장 정보
    Set<Long> loadJoinedRoomIds(Long userId, List<Long> roomIds);
}
