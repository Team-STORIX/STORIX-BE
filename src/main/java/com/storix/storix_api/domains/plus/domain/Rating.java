package com.storix.storix_api.domains.plus.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.storix.storix_api.global.apiPayload.exception.plus.InvalidRatingException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum Rating {

    ZERO_POINT_FIVE("0.5", 0.5),
    ONE("1.0", 1.0),
    ONE_POINT_FIVE("1.5", 1.5),
    TWO("2.0", 2.0),
    TWO_POINT_FIVE("2.5", 2.5),
    THREE("3.0", 3.0),
    THREE_POINT_FIVE("3.5", 3.5),
    FOUR("4.0", 4.0),
    FOUR_POINT_FIVE("4.5", 4.5),
    FIVE("5.0", 5.0);

    private final String dbValue;
    private final double ratingValue;

    @JsonCreator
    public static Rating from(String value) {
        return Arrays.stream(values())
                .filter(r -> r.dbValue.equals(value))
                .findFirst()
                .orElseThrow(() -> InvalidRatingException.EXCEPTION);
    }

    @JsonValue
    public String toJson() {
        return dbValue;
    }
}
