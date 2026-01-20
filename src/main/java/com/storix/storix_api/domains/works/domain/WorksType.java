package com.storix.storix_api.domains.works.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WorksType {

    WEBTOON("웹툰"),
    WEBNOVEL("웹소설"),
    COMIC("만화");

    private final String dbValue;
}
