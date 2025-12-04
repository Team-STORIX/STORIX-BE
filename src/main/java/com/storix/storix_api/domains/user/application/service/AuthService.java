package com.storix.storix_api.domains.user.application.service;

import com.storix.storix_api.controller.auth.dto.ArtistSignupRequest;
import com.storix.storix_api.controller.auth.dto.OAuthAuthorizationRequest;
import com.storix.storix_api.controller.auth.dto.ReaderSignupRequest;
import com.storix.storix_api.controller.auth.dto.ValidAuthDTO;
import com.storix.storix_api.domains.user.adaptor.AuthUserDetails;
import com.storix.storix_api.domains.user.adaptor.OAuthHelper;
import com.storix.storix_api.domains.user.adaptor.UserAdaptor;
import com.storix.storix_api.domains.user.domain.OAuthInfo;
import com.storix.storix_api.domains.user.domain.OAuthProvider;
import com.storix.storix_api.domains.user.dto.*;
import com.storix.storix_api.global.apiPayload.exception.user.DuplicateUserException;
import com.storix.storix_api.global.apiPayload.exception.user.UnknownUserException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserAdaptor userAdaptor;
    private final OAuthHelper oauthHelper;
    private final PasswordEncoder passwordEncoder;

    // 독자 회원 가입 가능 여부 (토큰 검증, 계정 정보 유무)
    // - 카카오
    @Transactional
    public ValidAuthDTO validKakaoSignup(OAuthAuthorizationRequest req) {

        KakaoTokenResponse kakaoToken = oauthHelper.getKakaoOAuthToken(req.authCode(), req.redirectUri());
        KakaoUserResponse kakaoUser = oauthHelper.getKakaoInformation(kakaoToken.accessToken());

        OAuthInfo oauthInfo = oauthHelper.getOauthInfoByIdToken(kakaoToken.idToken(), OAuthProvider.KAKAO);
        if (!oauthInfo.getOid().equals(kakaoUser.id())) { throw UnknownUserException.EXCEPTION; }

        boolean isRegistered = userAdaptor.isUserPresentWithProviderAndOid(OAuthProvider.KAKAO, kakaoUser.id());

        return new ValidAuthDTO(isRegistered, kakaoToken.idToken());
    }

    // - 네이버


    // 독자 회원 가입 (소셜 로그인)
    @Transactional
    public AuthUserDetails signUpReaderUser(ReaderSignupRequest req) {
        boolean isUserPresent = userAdaptor.isUserPresentWithProviderAndOid(req.oauthProvider(), req.oid());
        if (isUserPresent) throw DuplicateUserException.EXCEPTION;

        CreateReaderUserCommand m = new CreateReaderUserCommand(
                req.oauthProvider(),
                req.oid(),
                req.nickName(),
                req.gender(),
                req.favoriteGenre()
        );

        return userAdaptor.saveReaderUser(m);
    }


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
