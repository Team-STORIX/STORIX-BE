package com.storix.storix_api.domains.preference.repository;

import com.storix.storix_api.domains.preference.domain.PreferenceExploration;
import com.storix.storix_api.domains.works.domain.Works;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExplorationRepository extends JpaRepository<PreferenceExploration, Long> {

    // 1) 차트 집계: 특정 유저가 좋아요한 장르별 카운트
    @Query("SELECT w.genre, COUNT(w) FROM PreferenceExploration pe " +
            "JOIN Works w ON pe.worksId = w.id " +
            "WHERE pe.userId = :userId AND pe.isLiked = true " +
            "GROUP BY w.genre")
    List<Object[]> countLikedGenresByUserId(@Param("userId") Long userId);

    // 2) 중복 방지: 이미 응답한 작품 ID 목록
    @Query("SELECT pe.worksId FROM PreferenceExploration pe WHERE pe.userId = :userId")
    List<Long> findRespondedWorksIdsByUserId(@Param("userId") Long userId);

    // 3) 결과 조회: 좋아요/별로예요 탭 리스트 (팀원의 DTO 구조 활용)
    @Query("SELECT w FROM PreferenceExploration pe " +
            "JOIN Works w ON pe.worksId = w.id " +
            "WHERE pe.userId = :userId AND pe.isLiked = :isLiked")
    List<Works> findWorksByLikedStatus(
            @Param("userId") Long userId,
            @Param("isLiked") boolean isLiked
    );
}
