package com.storix.storix_api.domains.user.repository;

import com.storix.storix_api.domains.user.domain.UserFollow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserFollowRepository extends JpaRepository<UserFollow, Long> {
}
