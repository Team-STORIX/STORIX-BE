package com.storix.storix_api.domains.topicroom.application.port;

import com.storix.storix_api.domains.topicroom.domain.TopicRoom;
import com.storix.storix_api.domains.topicroom.domain.TopicRoomUser;
import org.springframework.data.domain.*;
import java.time.LocalDateTime;
import java.util.List;

public interface LoadTopicRoomPort {

    TopicRoom findById(Long roomId);

    Slice<TopicRoomUser> findParticipationsByUserId(Long userId, Pageable pageable);

    List<TopicRoom> findTop3Trending();

    Slice<TopicRoom> searchByWorksIdsOrDescription(List<Long> worksIds, String keyword, Pageable pageable);

    long countJoinedRooms(Long userId);

    boolean existsByUserIdAndRoomId(Long userId, Long roomId);

    LocalDateTime getLastMessageTime(Long roomId);
}