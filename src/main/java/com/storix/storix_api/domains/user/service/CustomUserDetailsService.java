package com.storix.storix_api.domains.user.service;

import com.storix.storix_api.domains.user.adaptor.CustomUserDetails;
import com.storix.storix_api.domains.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import com.storix.storix_api.domains.user.domain.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * username이 들어올 수 있는 경우:
     *  1) JWT 필터에서: subject = userId → "1", "2" 같은 숫자 문자열
     *  2) 로그인 시: loginId → "artist01" 같은 문자열
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 1) JWT 필터
        try {
            Long id = Long.valueOf(username);
            User user = userRepository.findById(id)
                    .orElseThrow(() ->
                            new UsernameNotFoundException("User not found with id: " + id));
            return new CustomUserDetails(user);

        } catch (NumberFormatException ignore) {
        }

        // 2) 로그인
        User user = userRepository.findArtistUserByLoginId(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with loginId: " + username));

        return new CustomUserDetails(user);
    }
}
