package com.storix.storix_api.domains.hashtag.repository;

import com.storix.storix_api.domains.hashtag.domain.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {

    Optional<Hashtag> findByName(String name);
}
