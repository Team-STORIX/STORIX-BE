package com.storix.storix_api.domains.user.adaptor;

import com.storix.storix_api.domains.user.domain.User;
import com.storix.storix_api.domains.user.repository.UserRepository;
import com.storix.storix_api.domains.user.service.CreateArtistUserCommand;
import com.storix.storix_api.global.apiPayload.code.ErrorCode;
import com.storix.storix_api.global.apiPayload.exception.ErrorResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserAdaptor {

    private final UserRepository userRepository;

    // 작가
    public Long findArtistUserIdByLoginId(String loginId){
        Optional<User> artistUser = userRepository.findArtistUserByLoginId(loginId);
        if(artistUser.isPresent()){
            return artistUser.get().getId();
        }
        return null;
    }

    public User findArtistUserByLoginId(String loginId){
        Optional<User> artistUser = userRepository.findArtistUserByLoginId(loginId);
        if(artistUser.isPresent()){
            return artistUser.get();
        }
        return null;
    }

    public AuthUserDetails findArtistUserIdAndRoleByLoginId(String loginId){
        Optional<User> artistUser = userRepository.findArtistUserByLoginId(loginId);
        if(artistUser.isPresent()){
            return new AuthUserDetails(String.valueOf(artistUser.get().getId()), String.valueOf(artistUser.get().getRole()));
        }
        return null;
    }

    public ErrorResponse isLoginIdDuplicate(String loginId) {
        Optional<User> artistUser = userRepository.findArtistUserByLoginId(loginId);
        if (artistUser.isPresent()) {
            return new ErrorResponse(ErrorCode.BAD_REQUEST);
        }
        return null;
    }

    @Transactional
    public User saveArtistUser(CreateArtistUserCommand cmd) {
        User user = userRepository.save(cmd.toEntity());
        return userRepository.save(user);
    }
}
