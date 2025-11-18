package com.storix.storix_api.domains.user.repository;

import com.storix.storix_api.domains.user.domain.UserMedia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserMediaRepository extends JpaRepository<UserMedia, Long> {
}
