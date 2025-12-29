package com.storix.storix_api.controller.auth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ReaderSocialLoginResponse(
        boolean isRegistered,
        ReaderLoginResponse readerLoginResponse,
        ReaderPreLoginResponse readerPreLoginResponse
) {
}
