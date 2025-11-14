package com.storix.storix_api.controller;

import com.storix.storix_api.controller.auth.LoginUseCase;
import com.storix.storix_api.controller.dto.ArtistLoginRequest;
import com.storix.storix_api.controller.dto.ArtistSignupRequest;
import com.storix.storix_api.controller.dto.ArtistSignupResponse;
import com.storix.storix_api.domains.user.service.AuthService;
import com.storix.storix_api.global.apiPayload.CustomResponse;
import com.storix.storix_api.global.apiPayload.code.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private AuthService authService;
    private LoginUseCase loginUseCase;

    @PostMapping("/users/artist/login")
    public ResponseEntity artistUserLogin(@RequestBody ArtistLoginRequest req){
        return ResponseEntity.ok()
                        .body(loginUseCase.artistLoginWithLoginId(req));
    }

    @Operation(summary = "백엔드용 작가 계정 생성 api 입니다.")
    @PostMapping("/developer/users/artist/signup")
    public CustomResponse<ArtistSignupResponse> developerArtistUserSignup(@RequestBody ArtistSignupRequest req){
        Long artistUserId = authService.signUpArtistUser(req);
        return CustomResponse.onSuccess(SuccessCode.SUCCESS, new ArtistSignupResponse(artistUserId, req.loginId(), req.nickName()));
    }

}
