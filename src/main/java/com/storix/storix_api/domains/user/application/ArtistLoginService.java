package com.storix.storix_api.domains.user.application;

import com.storix.storix_api.domains.user.adaptor.AuthUserDetails;
import com.storix.storix_api.domains.user.adaptor.UserAdaptor;
import com.storix.storix_api.domains.user.dto.LoginInfo;
import com.storix.storix_api.global.apiPayload.exception.user.ArtistLoginException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArtistLoginService implements UserDetailsService {

    private final UserAdaptor userAdaptor;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userAdaptor.findArtistUserIdAndRoleByLoginId(username);
    }

    public void validateArtistLogin (String loginId, String password) {
        LoginInfo artistUserLoginInfo = userAdaptor.findArtistUserLoginInfoByLoginId(loginId);

        if (!passwordEncoder.matches(password, artistUserLoginInfo.password())) {
            throw ArtistLoginException.EXCEPTION;
        }

    }
}
