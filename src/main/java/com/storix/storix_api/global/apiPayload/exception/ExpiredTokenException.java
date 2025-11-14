package com.storix.storix_api.global.apiPayload.exception;

import com.storix.storix_api.global.apiPayload.code.ErrorCode;

public class ExpiredTokenException extends STORIXCodeException {

    public static final STORIXCodeException EXCEPTION = new ExpiredTokenException();

    private ExpiredTokenException() { super(ErrorCode.TOKEN_EXPIRED); }
}