package com.storix.storix_api.domains.user.application.usecase.helper;

import com.storix.storix_api.domains.user.adaptor.JwtOIDCProvider;
import com.storix.storix_api.domains.user.application.client.KakaoInfoClient;
import com.storix.storix_api.domains.user.application.client.KakaoOAuthClient;
import com.storix.storix_api.domains.user.application.client.OAuthProperties;
import com.storix.storix_api.domains.user.domain.OAuthInfo;
import com.storix.storix_api.domains.user.domain.OAuthProvider;
import com.storix.storix_api.domains.user.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.storix.storix_api.global.apiPayload.STORIXStatic.BEARER;

@Service
@RequiredArgsConstructor
public class OAuthHelper {

    private final OAuthProperties oauthProperties;
    private final JwtOIDCProvider jwtOIDCProvider;

    // 카카오
    private final KakaoOAuthClient kakaoOauthClient;
    private final KakaoInfoClient kakaoInfoClient;

    // 네이버

    // 카카오: 인가 코드로 토큰 발급 요청 -> accessToken, idToken
    public KakaoTokenResponse getKakaoOAuthToken(String code, String redirectUri) {
        var kakao = oauthProperties.getKakao();
        return kakaoOauthClient.kakaoAuth(
                "authorization_code",
                kakao.getClientId(),
                redirectUri,
                code,
                kakao.getClientSecret()
        );
    }

    // 카카오: 사용자 정보 요청 (accessToken)
    public KakaoUserResponse getKakaoInformation(String accessToken) {
        return kakaoInfoClient.getUserInfo(BEARER + accessToken);
    }


    // OIDC 스펙: OIDC 공개키 목록 조회
    public OIDCPublicKeysResponse getOIDCPublicKeys(OAuthProvider provider) {
        switch (provider) {
            case KAKAO -> {
                return kakaoOauthClient.getKakaoOIDCOpenKeys();
            }
            // case NAVER -> {
            //      return naverOauthClient.getNaverOIDCOpenKeys();
            // }
            default -> {
                return null;
            }
        }
    }

    // OIDC 스펙: OIDC Config 정보 조회 (baseUrl, clientId)
    public OIDCConfigDTO getOIDCConfig(OAuthProvider provider) {
        switch (provider) {
            case KAKAO -> {
                var kakao = oauthProperties.getKakao();
                return new OIDCConfigDTO(kakao.getBaseUri(), kakao.getClientId());
            }
            // case NAVER ->  {
            //    var naver = oauthProperties.getNaver();
            //    return new OIDCConfigDTO(naver.getBaseUrl(), naver.getClientId());
            // }
            default -> {
                return null;
            }
        }
    }

    // OIDC 스펙: idToken 검증 후 OIDC Payload 반환 (iss, aud, sub)
    public OIDCDecodePayload getOIDCDecodePayload(String idToken, OAuthProvider provider) {

        OIDCConfigDTO oidcConfig = getOIDCConfig(provider);
        OIDCPublicKeysResponse oidcPublicKeysResponse = getOIDCPublicKeys(provider);

        return jwtOIDCProvider.getPayloadFromIdToken(
                idToken,
                oidcConfig.baseUri(),
                oidcConfig.clientId(),
                oidcPublicKeysResponse);
    }

    // OIDC 스펙: 검증된 idToken으로 OAuthInfo 반환 (provider, oid)
    public OAuthInfo getOauthInfoByIdToken(String idToken, OAuthProvider provider) {
        OIDCDecodePayload oidcDecodePayload = getOIDCDecodePayload(idToken, provider);
        return OAuthInfo.builder()
                .provider(provider)
                .oid(oidcDecodePayload.sub())
                .build();
    }

}
