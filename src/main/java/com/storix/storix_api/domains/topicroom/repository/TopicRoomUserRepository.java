package com.storix.storix_api.domains.topicroom.repository;

import com.storix.storix_api.domains.topicroom.domain.TopicRoomUser;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TopicRoomUserRepository extends JpaRepository<TopicRoomUser, Long> {

    @Query("select tru from TopicRoomUser tru join fetch tru.topicRoom where tru.userId = :userId")
    Slice<TopicRoomUser> findByUserIdWithTopicRoom(@Param("userId") Long userId, Pageable pageable);

    long countByUserId(Long userId);

    boolean existsByUserIdAndTopicRoomId(Long userId, Long topicRoomId);

    void deleteByUserIdAndTopicRoomId(Long userId, Long topicRoomId);
}