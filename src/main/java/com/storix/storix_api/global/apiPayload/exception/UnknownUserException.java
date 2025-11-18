package com.storix.storix_api.global.apiPayload.exception;

import com.storix.storix_api.global.apiPayload.code.ErrorCode;

public class UnknownUserException extends STORIXCodeException {

    public static final STORIXCodeException EXCEPTION = new UnknownUserException();

    private UnknownUserException() { super(ErrorCode.NOT_FOUND); }
}