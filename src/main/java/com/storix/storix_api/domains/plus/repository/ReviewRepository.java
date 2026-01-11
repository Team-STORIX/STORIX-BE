package com.storix.storix_api.domains.plus.repository;

import com.storix.storix_api.domains.plus.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    boolean existsByLibraryUserIdAndWorksId(Long libraryUserId, Long worksId);

}