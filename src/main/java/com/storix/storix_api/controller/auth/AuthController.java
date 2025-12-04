package com.storix.storix_api.controller.auth;

import com.storix.storix_api.controller.auth.dto.*;
import com.storix.storix_api.domains.user.application.usecase.AuthUseCase;
import com.storix.storix_api.domains.user.application.usecase.LoginUseCase;
import com.storix.storix_api.domains.user.application.usecase.OAuthLoginUseCase;
import com.storix.storix_api.domains.user.domain.OAuthProvider;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthUseCase authUseCase;
    private final LoginUseCase loginUseCase;
    private final OAuthLoginUseCase oauthLoginUseCase;

    @Operation(summary = "카카오 로그인", description = "카카오로 로그인 하는 api 입니다. 회원가입한 유저의 경우 JWT를, 아닌 경우 유저 정보 등록에 필요한 OAuthInfo를 반환합니다.")
    @GetMapping("/oauth/kakao/login")
    public ResponseEntity kakaoLogin(
            @RequestParam("code") String code,
            @RequestParam("redirectUri") String redirectUri
    ) {
        OAuthAuthorizationRequest req = new OAuthAuthorizationRequest(code, redirectUri);
        return ResponseEntity.ok()
                .body(oauthLoginUseCase.readerOAuthLogin(req, OAuthProvider.KAKAO));
    }

    // TODO: 네이버 로그인

    @Operation(summary = "독자 계정 회원가입", description = "유저 정보를 최종적으로 등록하는 api 입니다.")
    @PostMapping("/users/reader/signup")
    public ResponseEntity readerUserSignup(@RequestBody ReaderSignupRequest req){
        return ResponseEntity.ok()
                .body(authUseCase.readerSignup(req));
    }

    @Operation(summary = "작가 계정 일반 로그인", description = "작가 계정에 로그인 하는 api 입니다.")
    @PostMapping("/users/artist/login")
    public ResponseEntity artistUserLogin(@RequestBody ArtistLoginRequest req){
        return ResponseEntity.ok()
                        .body(loginUseCase.artistLoginWithLoginId(req));
    }

    @Operation(summary = "백엔드용 작가 계정 생성 api 입니다.")
    @PostMapping("/developer/users/artist/signup")
    public ResponseEntity developerArtistUserSignup(@RequestBody ArtistSignupRequest req){
        return ResponseEntity.ok()
                .body(authUseCase.artistSignup(req));
    }

}
