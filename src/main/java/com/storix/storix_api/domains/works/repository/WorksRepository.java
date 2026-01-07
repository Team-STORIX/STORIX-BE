package com.storix.storix_api.domains.works.repository;

import com.storix.storix_api.domains.works.domain.Works;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WorksRepository extends JpaRepository<Works, Long> {

    @Query("SELECT w FROM Works w " +
            "WHERE w.worksName LIKE %:keyword% " +
            "OR w.author LIKE %:keyword% " +
            "OR w.illustrator LIKE %:keyword% " +
            "OR w.originalAuthor LIKE %:keyword%")
    Slice<Works> findBySearchKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT w.id FROM Works w WHERE w.worksName LIKE %:keyword%")
    List<Long> findAllIdsByKeyword(@Param("keyword") String keyword);
}
