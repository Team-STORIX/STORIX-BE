package com.storix.storix_api.domains.user.application.usecase.helper;

import com.storix.storix_api.controller.auth.dto.LoginWithTokenResponse;
import com.storix.storix_api.controller.auth.dto.OAuthLoginWithTokenResponse;
import com.storix.storix_api.domains.user.adaptor.AuthUserDetails;
import com.storix.storix_api.domains.user.adaptor.TokenAdaptor;
import com.storix.storix_api.domains.user.adaptor.UserAdaptor;
import com.storix.storix_api.domains.user.domain.*;
import com.storix.storix_api.domains.user.dto.OnboardingTokenInfo;
import com.storix.storix_api.global.apiPayload.exception.user.InvalidTokenException;
import com.storix.storix_api.global.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.storix.storix_api.global.apiPayload.STORIXStatic.MILLI_TO_SECOND;

@Service
@RequiredArgsConstructor
public class TokenGenerateHelper {

    private final TokenProvider tokenProvider;
    private final TokenAdaptor tokenAdaptor;
    private final UserAdaptor userAdaptor;

    @Transactional
    public LoginWithTokenResponse generateLoginWithToken(AuthUserDetails userDetails) {

        Long userId = userDetails.getUserId();
        String role = userDetails.getRole();

        String accessToken = tokenProvider.createAccessToken(userId, role);
        String refreshToken = tokenProvider.createRefreshToken(userId);

        // redis 저장
        long ttlSeconds = tokenProvider.getRefreshTokenValidityMs() / MILLI_TO_SECOND;
        RefreshToken newRefreshToken = RefreshToken.builder()
                .id(userId)
                .refreshToken(refreshToken)
                .ttl(ttlSeconds)
                .build();
        tokenAdaptor.saveRefreshToken(newRefreshToken);

        return LoginWithTokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
    public String reissueAccessTokenWithRefreshToken(String refreshToken) {

        if (!tokenProvider.isRefreshToken(refreshToken)) {
            throw InvalidTokenException.EXCEPTION;
        }

        Long userId = tokenProvider.parseRefreshToken(refreshToken);
        Role role = userAdaptor.findUserRoleByUserId(userId);

        return tokenProvider.createAccessToken(userId, String.valueOf(role));
    }

    @Transactional
    public OAuthLoginWithTokenResponse generateOAuthLoginWithToken(OAuthInfo oAuthInfo) {

        OAuthProvider provider = oAuthInfo.getProvider();
        String oid = oAuthInfo.getOid();

        OnboardingTokenInfo oti = tokenProvider.createOnboardingToken();

        long ttlSeconds = tokenProvider.getOnboardingTokenValidityMs() / MILLI_TO_SECOND;
        OnboardingToken newOnboardingToken = OnboardingToken.builder()
                .jti(oti.jti())
                .provider(provider)
                .oid(oid)
                .onboardingToken(oti.onboardingToken())
                .ttl(ttlSeconds)
                .build();
        tokenAdaptor.saveOnboardingToken(newOnboardingToken);

        return OAuthLoginWithTokenResponse.builder()
                .onboardingToken(oti.onboardingToken())
                .build();
    }

}
