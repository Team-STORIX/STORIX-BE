package com.storix.storix_api.global.apiPayload.exception;

import com.storix.storix_api.global.apiPayload.code.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class GlobalExceptionHandler {

    @ExceptionHandler(STORIXCodeException.class)
    public ResponseEntity<ErrorResponse> STORIXCodeExceptionHandler(
            STORIXCodeException ex, HttpServletRequest request) {

        ErrorCode errorCode = ex.getErrorCode();
        ErrorResponse response = new ErrorResponse(errorCode);

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(response);
    }


}
