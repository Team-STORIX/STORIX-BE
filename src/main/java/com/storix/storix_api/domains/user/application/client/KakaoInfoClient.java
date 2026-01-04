package com.storix.storix_api.domains.user.application.client;

import com.storix.storix_api.domains.user.dto.KakaoUserResponse;
import com.storix.storix_api.global.config.web.KakaoInfoConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "KakaoInfoClient",
        url = "https://kapi.kakao.com",
        configuration = KakaoInfoConfig.class
)
public interface KakaoInfoClient {
    // 사용자 정보 조회
    @GetMapping("/v2/user/me")
    KakaoUserResponse getUserInfo(@RequestHeader("Authorization") String authorization);
}
