package com.storix.storix_api.domains.plus.adaptor;

import com.storix.storix_api.domains.plus.domain.Review;
import com.storix.storix_api.domains.plus.dto.CreateReviewCommand;
import com.storix.storix_api.domains.plus.repository.ReviewRepository;
import com.storix.storix_api.global.apiPayload.exception.plus.DuplicateReviewUploadException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReviewAdaptor {

    private final ReviewRepository reviewRepository;

    public Review saveReview(CreateReviewCommand cmd) {
        try {
            return reviewRepository.save(cmd.toEntity());
        } catch (DataIntegrityViolationException e) {
            throw DuplicateReviewUploadException.EXCEPTION;
        }
    }

    public void existsByUserAndWorks(Long userId, Long worksId) {
        boolean isReviewExist = reviewRepository.existsByLibraryUserIdAndWorksId(userId, worksId);
        if (isReviewExist) {
            throw DuplicateReviewUploadException.EXCEPTION;
        }
    }

}
