package com.storix.storix_api.domains.topicroom.repository;

import com.storix.storix_api.domains.topicroom.domain.TopicRoom;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TopicRoomRepository extends JpaRepository<TopicRoom, Long> {

    List<TopicRoom> findTop3ByCreatedAtAfterOrderByActiveUserNumberDesc(LocalDateTime threshold);

    @Query("SELECT t FROM TopicRoom t " +
            "WHERE t.id NOT IN :excludeIds " +
            "ORDER BY t.activeUserNumber DESC")
    List<TopicRoom> findTopAllTimeExcluding(@Param("excludeIds") List<Long> excludeIds, Pageable pageable);

    @Query("SELECT t FROM TopicRoom t WHERE t.worksId IN :worksIds OR t.topicRoomName LIKE %:keyword%")
    Slice<TopicRoom> findBySearchCondition(@Param("worksIds") List<Long> worksIds, @Param("keyword") String keyword, Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE TopicRoom t SET t.activeUserNumber = t.activeUserNumber + 1 WHERE t.id = :id")
    void incrementActiveUserNumber(@Param("id") Long id);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE TopicRoom t SET t.activeUserNumber = t.activeUserNumber - 1 " +
            "WHERE t.id = :id AND t.activeUserNumber > 0")
    void decrementActiveUserNumber(@Param("id") Long id);
}