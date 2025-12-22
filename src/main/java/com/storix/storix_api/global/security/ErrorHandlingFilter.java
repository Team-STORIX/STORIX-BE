package com.storix.storix_api.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.storix.storix_api.global.apiPayload.code.ErrorCode;
import com.storix.storix_api.global.apiPayload.exception.ErrorResponse;
import com.storix.storix_api.global.apiPayload.exception.STORIXCodeException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ErrorHandlingFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            filterChain.doFilter(request, response);

        } catch (STORIXCodeException ex) {
            if (response.isCommitted()) throw ex;

            ErrorCode errorCode = ex.getErrorCode();
            ErrorResponse body = new ErrorResponse(errorCode);

            response.setStatus(errorCode.getHttpStatus().value());
            response.setContentType("application/json;charset=UTF-8");
            objectMapper.writeValue(response.getWriter(), body);

        }
    }
}