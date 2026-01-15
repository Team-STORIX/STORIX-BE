package com.storix.storix_api.domains.plus.adaptor;

import com.storix.storix_api.domains.plus.domain.Rating;
import com.storix.storix_api.global.apiPayload.exception.plus.InvalidRatingException;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.math.BigDecimal;
import java.util.Arrays;

@Converter(autoApply = false)
public class RatingConverter implements AttributeConverter<Rating, BigDecimal> {

    @Override
    public BigDecimal convertToDatabaseColumn(Rating rating) {
        if (rating == null) return null;
        return BigDecimal.valueOf(rating.getRatingValue());
    }

    @Override
    public Rating convertToEntityAttribute(BigDecimal value) {
        if (value == null) return null;

        double doubleValue = value.doubleValue();

        return Arrays.stream(Rating.values())
                .filter(r -> Double.compare(r.getRatingValue(), doubleValue) == 0)
                .findFirst()
                .orElseThrow(() -> InvalidRatingException.EXCEPTION);
    }
}
