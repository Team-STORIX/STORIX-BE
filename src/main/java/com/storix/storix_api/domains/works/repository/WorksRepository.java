package com.storix.storix_api.domains.works.repository;

import com.storix.storix_api.domains.works.domain.Works;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WorksRepository extends JpaRepository<Works, Long> {

    // 1. 작품 검색 (작품명, 작가명 포함)
    Slice<Works> findByWorksNameContainingOrArtistNameContaining(String worksName, String artistName, Pageable pageable);

    // 2. 작가명 검색 (String)
    @Query("SELECT DISTINCT w.artistName FROM Works w WHERE w.artistName LIKE %:keyword%")
    List<String> findDistinctArtistNames(@Param("keyword") String keyword, Pageable pageable);
}
