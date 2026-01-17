package com.storix.storix_api.domains.plus.application.service;

import com.storix.storix_api.domains.library.adaptor.LibraryAdaptor;
import com.storix.storix_api.domains.plus.adaptor.ReviewAdaptor;
import com.storix.storix_api.domains.plus.controller.dto.ReaderReviewRedirectResponse;
import com.storix.storix_api.domains.plus.controller.dto.ReaderReviewUploadRequest;
import com.storix.storix_api.domains.plus.domain.Review;
import com.storix.storix_api.domains.plus.dto.CreateReviewCommand;
import com.storix.storix_api.domains.user.application.port.LoadUserPort;
import com.storix.storix_api.domains.works.application.port.LoadWorksPort;
import com.storix.storix_api.global.apiPayload.exception.topicRoom.UnverifiedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewAdaptor reviewAdaptor;
    private final LibraryAdaptor libraryAdaptor;

    private final LoadWorksPort loadWorksPort;
    private final LoadUserPort loadUserPort;

    @Transactional
    public ReaderReviewRedirectResponse createReview(Long userId, ReaderReviewUploadRequest req) {

        reviewAdaptor.existsByUserAndWorks(userId, req.worksId());

        if (loadWorksPort.isWorksForAdult(req.worksId())) {

            Boolean isAdult = loadUserPort.findIsAdultVerifiedById(userId);

            if (!Boolean.TRUE.equals(isAdult)) {
                throw UnverifiedException.EXCEPTION;
            }
        }

        CreateReviewCommand cmd = new CreateReviewCommand(
                userId,
                req.worksId(),
                req.isSpoiler(),
                req.rating(),
                req.content()
        );

        Review review = reviewAdaptor.saveReview(cmd);

        // 서재 도메인 업데이트
        libraryAdaptor.incrementReviewCount(userId);

        // 작품 도메인 업데이트
        loadWorksPort.updateIncrementingReviewInfoToWorks(req.worksId(), req.rating().getRatingValue());

        return new ReaderReviewRedirectResponse(review.getWorksId(), review.getLibraryUserId(), review.getId());
    }

}