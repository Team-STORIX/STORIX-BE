package com.storix.storix_api.domains.works.repository;

import com.storix.storix_api.domains.works.domain.Works;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WorksRepository extends JpaRepository<Works, Long> {

    Slice<Works> findByWorksNameContaining(String keyword, Pageable pageable);
}
