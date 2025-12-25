package com.storix.storix_api.controller.auth;

import com.storix.storix_api.controller.auth.dto.*;
import com.storix.storix_api.domains.user.adaptor.OnboardingUserDetails;
import com.storix.storix_api.domains.user.application.usecase.AuthUseCase;
import com.storix.storix_api.domains.user.application.usecase.AuthorizationUseCase;
import com.storix.storix_api.domains.user.application.usecase.LoginUseCase;
import com.storix.storix_api.domains.user.application.usecase.OAuthLoginUseCase;
import com.storix.storix_api.domains.user.domain.OAuthProvider;
import com.storix.storix_api.global.apiPayload.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "온보딩", description = "온보딩 관련 API")
public class AuthController {

    private final AuthUseCase authUseCase;
    private final LoginUseCase loginUseCase;
    private final OAuthLoginUseCase oauthLoginUseCase;
    private final AuthorizationUseCase authorizationUseCase;

    @Operation(summary = "카카오 로그인", description = "카카오로 로그인 하는 api 입니다. 회원가입한 유저의 경우 액세스 토큰 & 리프레쉬 토큰을, 아닌 경우 유저 정보 등록에 필요한 온보딩 토큰을 반환합니다.")
    @GetMapping("/oauth/kakao/login")
    public ResponseEntity<CustomResponse<ReaderSocialLoginResponse>> kakaoLogin(
            @RequestParam("code") String code,
            @RequestParam("redirectUri") String redirectUri
    ) {
        OAuthAuthorizationRequest req = OAuthAuthorizationRequest.forKakao(code, redirectUri);
        return ResponseEntity.ok()
                .body(oauthLoginUseCase.readerOAuthLogin(req, OAuthProvider.KAKAO));
    }

    @Operation(summary = "네이버 로그인", description = "네이버로 로그인 하는 api 입니다. 회원가입한 유저의 경우 액세스 토큰 & 리프레쉬 토큰을, 아닌 경우 유저 정보 등록에 필요한 온보딩 토큰을 반환합니다.")
    @GetMapping("/oauth/naver/login")
    public ResponseEntity<CustomResponse<ReaderSocialLoginResponse>> naverLogin(
            @RequestParam("code") String code,
            @RequestParam("state") String state
    ) {
        OAuthAuthorizationRequest req = OAuthAuthorizationRequest.forNaver(code, state);
        return ResponseEntity.ok()
                .body(oauthLoginUseCase.readerOAuthLogin(req, OAuthProvider.NAVER));
    }

    @Operation(summary = "독자 계정 회원가입", description = "유저 정보를 최종적으로 등록하는 api 입니다.")
    @PostMapping("/users/reader/signup")
    public ResponseEntity<CustomResponse<LoginWithTokenResponse>> readerUserSignup(
            @AuthenticationPrincipal OnboardingUserDetails onboardingUser,
            @RequestBody ReaderSignupRequest req){
        return ResponseEntity.ok()
                .body(authUseCase.readerSignup(req, onboardingUser.getJti()));
    }

    @Operation(summary = "작가 계정 일반 로그인", description = "작가 계정에 로그인 하는 api 입니다.")
    @PostMapping("/users/artist/login")
    public ResponseEntity<CustomResponse<LoginWithTokenResponse>> artistUserLogin(
            @RequestBody ArtistLoginRequest req){
        return ResponseEntity.ok()
                        .body(loginUseCase.artistLoginWithLoginId(req));
    }

    @Operation(summary = "[백엔드용] 작가 계정 회원가입", description = "백엔드용 작가 계정 생성 api 입니다.")
    @PostMapping("/developer/users/artist/signup")
    public ResponseEntity<CustomResponse<ArtistSignupResponse>> developerArtistUserSignup(
            @RequestBody ArtistSignupRequest req){
        return ResponseEntity.ok()
                .body(authUseCase.artistSignup(req));
    }

    @Operation(summary = "액세스 토큰 재발급", description = "만료된 AccessToken을 재발급해주기 위해서 RefreshToken을 받는 api 입니다.")
    @PostMapping("/refresh_token")
    public ResponseEntity<CustomResponse<AuthorizationResponse>> reissueAccessToken(
            @RequestBody RefreshTokenRequest req){
        return ResponseEntity.ok()
                .body(authorizationUseCase.getAccessTokenWithRefreshToken(req));
    }

    @Operation(summary = "로그아웃", description = "로그아웃 용 api 입니다. refreshToken을 보내주세요")
    @PostMapping("/user/logout")
    public ResponseEntity<CustomResponse> logout(
            @RequestBody LogoutRequest req) {
        return ResponseEntity.ok()
                .body(loginUseCase.userLogoutWithRefreshToken(req));
    }

}
