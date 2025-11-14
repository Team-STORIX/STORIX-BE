package com.storix.storix_api.global.apiPayload.exception;

import com.storix.storix_api.global.apiPayload.code.ErrorCode;

public class ExpiredRefreshTokenException extends STORIXCodeException {

    public static final STORIXCodeException EXCEPTION = new ExpiredRefreshTokenException();

    private ExpiredRefreshTokenException() { super(ErrorCode.TOKEN_EXPIRED); }
}