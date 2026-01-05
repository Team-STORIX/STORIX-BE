package com.storix.storix_api.domains.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Role {
    READER("독자"),
    ARTIST("작가");

    private final String value;
}
