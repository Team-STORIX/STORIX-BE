package com.storix.storix_api.domains.topicroom.domain;

import com.storix.storix_api.global.model.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TopicRoom extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "topic_room_id")
    private Long id;

    @Column(name = "topic_room_name", nullable = false)
    private String topicRoomName;

    @Column(name = "works_id", nullable = false)
    private Long worksId;

    @Column(name = "active_user_number", nullable = false)
    private Integer activeUserNumber = 0;

    @Builder
    public TopicRoom(String topicRoomName, Long worksId) {
        this.topicRoomName = topicRoomName;
        this.worksId = worksId;
    }

}
