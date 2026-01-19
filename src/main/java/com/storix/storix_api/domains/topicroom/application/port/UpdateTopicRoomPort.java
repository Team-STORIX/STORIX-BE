package com.storix.storix_api.domains.topicroom.application.port;

import java.time.LocalDateTime;

public interface UpdateTopicRoomPort {
    void updateLastChatTime(Long roomId, LocalDateTime lastChatTime);
}
