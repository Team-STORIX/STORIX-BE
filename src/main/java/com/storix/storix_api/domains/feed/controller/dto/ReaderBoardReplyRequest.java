package com.storix.storix_api.domains.feed.controller.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ReaderBoardReplyRequest(

        @NotNull(message = "댓글 내용을 입력해주세요.")
        @Size(max = 300, message = "댓글은 300자까지 가능합니다.")
        String comment
) {
}
