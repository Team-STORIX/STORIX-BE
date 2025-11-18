package com.storix.storix_api.global.apiPayload.exception;

import com.storix.storix_api.global.apiPayload.code.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(STORIXCodeException.class)
    public ResponseEntity<ErrorResponse> STORIXCodeExceptionHandler (STORIXCodeException ex) {

        ErrorCode errorCode = ex.getErrorCode();
        ErrorResponse response = new ErrorResponse(errorCode);

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(response);
    }


}
