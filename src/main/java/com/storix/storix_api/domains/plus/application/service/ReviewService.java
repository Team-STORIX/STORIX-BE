package com.storix.storix_api.domains.plus.application.service;

import com.storix.storix_api.domains.library.adaptor.LibraryAdaptor;
import com.storix.storix_api.domains.plus.adaptor.ReviewAdaptor;
import com.storix.storix_api.domains.plus.controller.dto.ReaderReviewRedirectResponse;
import com.storix.storix_api.domains.plus.controller.dto.ReaderReviewUploadRequest;
import com.storix.storix_api.domains.plus.domain.Review;
import com.storix.storix_api.domains.plus.dto.CreateReviewCommand;
import com.storix.storix_api.domains.works.application.helper.AdultWorksHelper;
import com.storix.storix_api.domains.works.application.port.LoadWorksPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewAdaptor reviewAdaptor;
    private final LibraryAdaptor libraryAdaptor;

    private final LoadWorksPort loadWorksPort;

    private final AdultWorksHelper adultWorksHelper;

    @Transactional
    public ReaderReviewRedirectResponse createReview(Long userId, ReaderReviewUploadRequest req) {

        reviewAdaptor.existsByUserAndWorks(userId, req.worksId());

        // 성인 작품 여부 확인 및 핸들링
        adultWorksHelper.CheckUserAuthorityWithWorks(userId, req.worksId());

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