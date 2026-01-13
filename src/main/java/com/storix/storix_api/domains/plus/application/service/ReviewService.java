package com.storix.storix_api.domains.plus.application.service;

import com.storix.storix_api.domains.library.LibraryAdaptor;
import com.storix.storix_api.domains.plus.adaptor.ReviewAdaptor;
import com.storix.storix_api.domains.plus.controller.dto.ReaderReviewRedirectResponse;
import com.storix.storix_api.domains.plus.controller.dto.ReaderReviewUploadRequest;
import com.storix.storix_api.domains.plus.domain.Review;
import com.storix.storix_api.domains.plus.dto.CreateReviewCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewAdaptor reviewAdaptor;
    private final LibraryAdaptor libraryAdaptor;

    @Transactional
    public ReaderReviewRedirectResponse createReview(Long userId, ReaderReviewUploadRequest req) {

        reviewAdaptor.existsByUserAndWorks(userId, req.worksId());

        CreateReviewCommand cmd = new CreateReviewCommand(
                userId,
                req.worksId(),
                req.isSpoiler(),
                req.rating(),
                req.content()
        );

        Review review = reviewAdaptor.saveReview(cmd);
        libraryAdaptor.incrementReviewCount(userId);

        return new ReaderReviewRedirectResponse(review.getWorksId(), review.getLibraryUserId(), review.getId());
    }

}