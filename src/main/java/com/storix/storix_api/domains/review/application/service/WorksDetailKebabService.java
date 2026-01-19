package com.storix.storix_api.domains.review.application.service;

import com.storix.storix_api.domains.plus.adaptor.ReviewAdaptor;
import com.storix.storix_api.domains.review.adaptor.ReviewLikeAdaptor;
import com.storix.storix_api.domains.review.adaptor.ReviewReportAdaptor;
import com.storix.storix_api.domains.review.controller.dto.ModifyReviewRequest;
import com.storix.storix_api.domains.review.controller.dto.ReviewReportRequest;
import com.storix.storix_api.domains.review.dto.CreateWorksDetailReportCommand;
import com.storix.storix_api.global.apiPayload.exception.topicRoom.SelfReportException;
import com.storix.storix_api.global.apiPayload.exception.user.ForbiddenApproachException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WorksDetailKebabService {

    private final ReviewAdaptor reviewAdaptor;
    private final ReviewLikeAdaptor reviewLikeAdaptor;
    private final ReviewReportAdaptor reviewReportAdaptor;

    @Transactional
    public Long changeReviewDetail(Long userId, Long reviewId, ModifyReviewRequest req) {
        Long reviewerId = reviewAdaptor.findReviewerIdById(reviewId);
        if (!reviewerId.equals(userId)) {
            throw ForbiddenApproachException.EXCEPTION;
        }

        return reviewAdaptor.updateReviewDetail(reviewId, req);
    }

    @Transactional
    public void deleteReview(Long userId, Long reviewId) {
        Long reviewerId = reviewAdaptor.findReviewerIdById(reviewId);
        if (!reviewerId.equals(userId)) {
            throw ForbiddenApproachException.EXCEPTION;
        }

        // 연관관계 맺은 리뷰 좋아요 삭제
        reviewLikeAdaptor.deleteAllRelatedReviewLike(reviewId);

        reviewAdaptor.deleteReview(userId, reviewId);
    }

    @Transactional
    public void reportReview(Long userId, Long reviewId, ReviewReportRequest req) {

        if (userId.equals(req.reportedUserId())) {
            throw SelfReportException.EXCEPTION;
        }

        CreateWorksDetailReportCommand cmd = new CreateWorksDetailReportCommand(
                userId,
                req.reportedUserId(),
                reviewId,
                req.reason(),
                req.otherReason()
        );

        reviewReportAdaptor.saveReport(cmd);
    }

}
