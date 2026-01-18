package com.storix.storix_api.domains.plus.repository;

import com.storix.storix_api.domains.plus.domain.ReaderBoard;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReaderBoardRepository extends JpaRepository<ReaderBoard, Long> {

    // 프로필 관련
    @Query("SELECT rb " +
            "FROM ReaderBoard rb " +
            "WHERE rb.userId = :userId ")
    Slice<ReaderBoard> findAllReaderBoardByUserId(Long userId, Pageable pageable);

}
