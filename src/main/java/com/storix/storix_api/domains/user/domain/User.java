package com.storix.storix_api.domains.user.domain;

import com.storix.storix_api.global.model.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(
        name = "users",
        indexes = {
                @Index(name = "idx_loginId_password", columnList = "loginId, password")
        }
)
public class User extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    // 계정 정보
    @Column(nullable = false)
    private String nickName;
    @Embedded private Profile profile;
    private Boolean isAdultVerified = false;

    // 계정 상태
    private AccountState accountState = AccountState.NORMAL;

    // 계정 권한
    private LocalDateTime lastLoginAt = LocalDateTime.now();
    private Role role = Role.READER;

    // 독자용 소셜 로그인
    private String name;
    @Embedded private OAuthInfo oauthInfo;

    // 작가용 아이디/비번
    private String loginId = null;
    private String password = null;

    /** 생성자 로직 **/
    protected User() {}

    @Builder // 독자
    public User(OAuthInfo oauthInfo, String name, String nickName, Profile profile) {
        this.oauthInfo = oauthInfo;
        this.name = name;
        this.nickName = nickName;
        this.profile = profile;
    }

    @Builder // 작가
    public User(String nickName, String loginId, String password) {
        this.nickName = nickName;
        this.loginId = loginId;
        this.password = password;
        this.role = Role.ARTIST;
    }

    /** 비즈니스 로직 **/
    public void login() {
        // 계정 상태 = 정지 -> 로그인 제한 Exception
        lastLoginAt = LocalDateTime.now();
    }

    // 계정 정보 수정

    // 계정 탈퇴

}
