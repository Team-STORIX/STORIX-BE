package com.storix.storix_api.domains.works.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Platform {

    NAVER_WEBTOON("네이버 웹툰"),
    KAKAO_WEBTOON("카카오 웹툰"),
    RIDI("리디");

    private final String dbValue;
}
