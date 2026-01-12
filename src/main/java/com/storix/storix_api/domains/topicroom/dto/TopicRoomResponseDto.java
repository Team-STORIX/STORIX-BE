package com.storix.storix_api.domains.topicroom.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TopicRoomResponseDto {

    private Long topicRoomId;
    private String topicRoomName;
    private String worksType;
    private String worksName;
    private String thumbnailUrl;
    private Integer activeUserNumber;
    private String lastChatTime;
    private Boolean isJoined;

}
