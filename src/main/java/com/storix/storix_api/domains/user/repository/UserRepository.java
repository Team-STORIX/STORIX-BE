package com.storix.storix_api.domains.user.repository;

import com.storix.storix_api.domains.user.domain.OAuthProvider;
import com.storix.storix_api.domains.user.domain.Role;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import com.storix.storix_api.domains.user.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByNickName(String nickName);

    Optional<User> findArtistUserByLoginId(String loginId);

    Optional<User> findByOauthInfoProviderAndOauthInfoOid(
            OAuthProvider provider,
            String oid
    );

    Slice<User> findByRoleAndNickNameContaining(Role role, String nickName, Pageable pageable);
}
