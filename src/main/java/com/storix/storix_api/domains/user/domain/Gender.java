package com.storix.storix_api.domains.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Gender {
    MALE("남성"),
    FEMALE("여성");

    private String value;
}
