package com.storix.storix_api.domains.topicroom.repository;

import com.storix.storix_api.domains.topicroom.domain.TopicRoomUser;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicRoomUserRepository extends JpaRepository<TopicRoomUser, Long> {

    Slice<TopicRoomUser> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    long countByUserId(Long userId);

    boolean existsByUserIdAndTopicRoomId(Long userId, Long topicRoomId);

    void deleteByUserIdAndTopicRoomId(Long userId, Long topicRoomId);
}