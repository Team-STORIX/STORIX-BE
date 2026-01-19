package com.storix.storix_api.domains.feed.dto;

public record StandardReplyInfoWithLike(
        Long replyId,
        Long userId,
        String comment,
        String lastCreatedTime,
        int likeCount,
        boolean isLiked
) {
    public static StandardReplyInfoWithLike of(ReaderBoardReplyInfo reply, boolean isLiked) {
        return new StandardReplyInfoWithLike(
                reply.replyId(),
                reply.userId(),
                reply.comment(),
                reply.lastCreatedTime(),
                reply.likeCount(),
                isLiked
        );
    }
}