package com.storix.storix_api.global.apiPayload.exception;

public record FieldErrorResponse(
        String field,
        Object rejectedValue,
        String reason,
        String message
) {}
