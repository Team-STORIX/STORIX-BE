package com.storix.storix_api.domains.plus.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.storix.storix_api.domains.plus.domain.ReaderBoard;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ReaderBoardInfo(
        // 유저 정보
        Long userId,

        // 게시글 정보
        Long boardId,
        Boolean isWorksSelected,
        Long worksId,
        String lastCreatedTime,
        String content,
        int likeCount,
        int replyCount,

        // 좋아요 여부
        boolean isLiked
) {
    // 내 게시글 조회
    public static ReaderBoardInfo ofMyBoard(ReaderBoard board, boolean isLiked) {
        return new ReaderBoardInfo(
                null,
                board.getId(),
                board.isWorksSelected(),
                board.getWorksId(),
                formatTimeAgo(board.getCreatedAt()),
                board.getContent(),
                board.getLikeCount(),
                board.getReplyCount(),
                isLiked
        );
    }

    // 피드 게시글 조회
    public static ReaderBoardInfo ofFeedBoard(ReaderBoard board, boolean isLiked) {
        return new ReaderBoardInfo(
                board.getUserId(),
                board.getId(),
                board.isWorksSelected(),
                board.getWorksId(),
                formatTimeAgo(board.getCreatedAt()),
                board.getContent(),
                board.getLikeCount(),
                board.getReplyCount(),
                isLiked
        );
    }

    // 오늘의 피드 게시글 조회
    public static ReaderBoardInfo ofHomeBoard(ReaderBoard board, boolean isLiked) {
        return new ReaderBoardInfo(
                board.getUserId(),
                board.getId(),
                null,
                null,
                null,
                board.getContent(),
                board.getLikeCount(),
                board.getReplyCount(),
                isLiked
        );
    }


    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");

    private static String formatTimeAgo(LocalDateTime time) {
        LocalDateTime now = LocalDateTime.now();
        long minutes = Duration.between(time, now).toMinutes();

        if (minutes < 1) { return "방금 전"; }
        if (minutes < 60) { return minutes + "분 전"; }

        long hours = minutes / 60;
        if (hours < 24) { return hours + "시간 전"; }

        long days = hours / 24;
        if (days < 7) { return days + "일 전"; }

        return time.format(DATE_TIME_FORMATTER);
    }
}