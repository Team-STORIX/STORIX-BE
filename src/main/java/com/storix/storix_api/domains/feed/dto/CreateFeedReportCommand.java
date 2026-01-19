package com.storix.storix_api.domains.feed.dto;

import com.storix.storix_api.domains.feed.domain.FeedReport;
import com.storix.storix_api.domains.topicroom.domain.enums.ReportReason;

public record CreateFeedReportCommand (
        Long reporterId,
        Long reportedUserId,
        Long boardId,
        ReportReason reason,
        String otherReason
) {
    public FeedReport toEntity() {
        return new FeedReport(
                reporterId,
                reportedUserId,
                boardId,
                reason,
                otherReason
        );
    }
}
