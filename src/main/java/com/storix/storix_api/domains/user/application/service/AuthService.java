package com.storix.storix_api.domains.user.application.service;

import com.storix.storix_api.controller.auth.dto.ArtistSignupRequest;
import com.storix.storix_api.domains.user.adaptor.UserAdaptor;
import com.storix.storix_api.domains.user.dto.CreateArtistUserCommand;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserAdaptor userAdaptor;
    private final PasswordEncoder passwordEncoder;

    // 독자 회원 가입 (소셜 로그인)


    // 작가 회원 가입 (일반 로그인)
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
        // TODO: 이때 WorksAndArtistMatcher 만들어두고 artistUserId 바로 넘기면 될듯요 (회원가입과 동시에 Works에 작가 회원id 정보 넣어주기)

        return artistUserId;
    }
}
