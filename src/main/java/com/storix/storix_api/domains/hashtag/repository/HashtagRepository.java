package com.storix.storix_api.domains.hashtag.repository;

import com.storix.storix_api.domains.hashtag.domain.Hashtag;
import com.storix.storix_api.domains.hashtag.dto.HashtagInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {

    Optional<Hashtag> findByName(String name);

    @Query("SELECT new com.storix.storix_api.domains.hashtag.dto.HashtagInfo(w.id, h.name) " +
            "FROM Hashtag  h " +
            "JOIN h.works w "+
            "WHERE w.id IN :worksIds " +
            "ORDER BY w.id, h.name")
    List<HashtagInfo> findAllByWorksIds(@Param("worksIds") List<Long> worksIds);

}
