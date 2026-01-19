package com.storix.storix_api.domains.feed.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record FeedReportRequest(
        @NotBlank(message = "신고 대상인 사용자의 id 값을 보내주세요.")
        Long reportedUserId
) {
}
