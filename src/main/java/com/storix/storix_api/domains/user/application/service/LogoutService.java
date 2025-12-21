package com.storix.storix_api.domains.user.application.service;

import com.storix.storix_api.domains.user.adaptor.TokenAdaptor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LogoutService {

    private final TokenAdaptor tokenAdaptor;

    @Transactional
    public void logoutByRefreshToken(String refreshToken) {
        Long userId = tokenAdaptor.findUserIdByRefreshToken(refreshToken);
        tokenAdaptor.deleteRefreshTokenByUserId(userId);
    }

}
