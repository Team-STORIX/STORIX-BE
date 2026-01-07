package com.storix.storix_api.domains.topicroom.repository;

import com.storix.storix_api.domains.topicroom.domain.TopicRoom;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface TopicRoomRepository extends JpaRepository<TopicRoom, Long> {

    List<TopicRoom> findTop3ByOrderByActiveUserNumberDesc();

    @Query("SELECT t FROM TopicRoom t WHERE t.worksId IN :worksIds OR t.topicRoomName LIKE %:keyword%")
    Slice<TopicRoom> findBySearchCondition(@Param("worksIds") List<Long> worksIds, @Param("keyword") String keyword, Pageable pageable);
}