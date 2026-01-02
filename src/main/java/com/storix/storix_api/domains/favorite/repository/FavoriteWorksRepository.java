package com.storix.storix_api.domains.favorite.repository;

import com.storix.storix_api.domains.favorite.domain.FavoriteWorks;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteWorksRepository extends JpaRepository<FavoriteWorks, Long> {
    void deleteByUserId(Long userId);
}
