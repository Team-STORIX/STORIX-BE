package com.storix.storix_api.global.apiPayload.exception.user;

import com.storix.storix_api.global.apiPayload.code.ErrorCode;
import com.storix.storix_api.global.apiPayload.exception.STORIXCodeException;

public class InsufficientFavoriteWorksException extends STORIXCodeException {

    public static final STORIXCodeException EXCEPTION = new InsufficientFavoriteWorksException();

    private InsufficientFavoriteWorksException() { super(ErrorCode.ONBOARDING_INSUFFICIENT_FAVORITE_WORKS); }
}
