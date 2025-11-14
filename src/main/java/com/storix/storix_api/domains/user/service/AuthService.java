package com.storix.storix_api.domains.user.service;

import com.storix.storix_api.controller.dto.ArtistSignupRequest;
import com.storix.storix_api.domains.user.adaptor.UserAdaptor;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserAdaptor userAdaptor;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long signUpArtistUser(ArtistSignupRequest req) {
        userAdaptor.isLoginIdDuplicate(req.loginId());

        CreateArtistUserCommand m = new CreateArtistUserCommand(
                req.nickName(),
                req.loginId(),
                passwordEncoder.encode(req.password())
        );

        userAdaptor.saveArtistUser(m);
        Long artistUserId = userAdaptor.findArtistUserIdByLoginId(req.loginId());

        return artistUserId;
    }
}
