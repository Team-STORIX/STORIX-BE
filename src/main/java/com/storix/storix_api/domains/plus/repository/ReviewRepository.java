package com.storix.storix_api.domains.plus.repository;

import com.storix.storix_api.domains.plus.domain.Review;
import com.storix.storix_api.domains.plus.dto.ReviewedWorksIdAndRatingInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    boolean existsByLibraryUserIdAndWorksId(Long libraryUserId, Long worksId);

    @Query("SELECT new com.storix.storix_api.domains.plus.dto.ReviewedWorksIdAndRatingInfo(r.worksId, r.id, r.rating) " +
            "FROM Review r " +
            "WHERE r.libraryUserId = :userId")
    Slice<ReviewedWorksIdAndRatingInfo> findWorksIdsByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT new com.storix.storix_api.domains.plus.dto.ReviewedWorksIdAndRatingInfo(r.worksId, r.id, r.rating) " +
            "FROM Review r " +
            "WHERE r.libraryUserId = :userId")
    List<ReviewedWorksIdAndRatingInfo> findAllWorksIdsByUserId(@Param("userId") Long userId);

    @Query("SELECT new com.storix.storix_api.domains.plus.dto.ReviewedWorksIdAndRatingInfo(r.worksId, r.id, r.rating) " +
            "FROM Review r " +
            "WHERE r.libraryUserId = :userId AND r.worksId IN :worksIds")
    List<ReviewedWorksIdAndRatingInfo> findAllReviewInfoByFavoriteWorks(@Param("userId") Long userId,
                                                                        @Param("worksIds") List<Long> worksIds);

}