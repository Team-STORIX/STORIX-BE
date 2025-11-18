package com.storix.storix_api.domains.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.storix.storix_api.domains.user.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findArtistUserByLoginId(String loginId);
}
