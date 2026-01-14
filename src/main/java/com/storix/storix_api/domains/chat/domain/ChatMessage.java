package com.storix.storix_api.domains.chat.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {

    private MessageType messageType;
    private Long roomId;
    private Long senderId;
    private String senderName;
    private String message;

}
