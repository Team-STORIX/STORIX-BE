package com.storix.storix_api.global.apiPayload.exception;

import com.storix.storix_api.global.apiPayload.code.ErrorCode;

public class ArtistLoginException extends STORIXCodeException {

    public static final STORIXCodeException EXCEPTION = new ArtistLoginException();

    private ArtistLoginException() { super(ErrorCode.FAILED_ARTIST_LOGIN); }
}