package com.storix.storix_api.domains.topicroom.repository;

import com.storix.storix_api.domains.topicroom.domain.TopicRoomUser;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TopicRoomUserRepository extends JpaRepository<TopicRoomUser, Long> {

    @Query("select tru from TopicRoomUser tru join fetch tru.topicRoom where tru.userId = :userId")
    Slice<TopicRoomUser> findByUserIdWithTopicRoom(@Param("userId") Long userId, Pageable pageable);

    long countByUserId(Long userId);

    boolean existsByUserIdAndTopicRoomId(Long userId, Long topicRoomId);

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM TopicRoomUser tru WHERE tru.userId = :userId AND tru.topicRoom.id = :roomId")
    int deleteByUserIdAndTopicRoomId(@Param("userId") Long userId, @Param("roomId") Long roomId);

    // 특정 유저가 참여 중인 모든 방 ID 조회
    @Query("SELECT tru.topicRoom.id FROM TopicRoomUser tru WHERE tru.userId = :userId")
    List<Long> findAllJoinedRoomIdsByUserId(@Param("userId") Long userId);
}