package com.storix.storix_api.domains.user.domain;

import com.storix.storix_api.domains.works.domain.Genre;
import com.storix.storix_api.global.apiPayload.exception.user.AlreadyWithDrawUserException;
import com.storix.storix_api.global.model.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Table(
        name = "users",
        indexes = {
                @Index(name = "idx_loginId", columnList = "loginId")
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_oauth_provider_oid",
                        columnNames = {"oauth_provider", "oauth_oid"}
                )
        }
)
public class User extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    // 계정 정보
    @Column(name = "nick_name", nullable = false, length = 10)
    private String nickName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @ElementCollection(targetClass = Genre.class)
    @CollectionTable(
            name = "user_favorite_genre",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "genre", nullable = false)
    private Set<Genre> favoriteGenreList = new HashSet<>();

    @Column(name = "profile_image_url")
    private String profileImageUrl = null;

    @Column(name = "is_adult_verified")
    private Boolean isAdultVerified = false;

    // 계정 상태
    @Column(name = "account_state")
    private AccountState accountState = AccountState.NORMAL;

    // 계정 권한
    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt = LocalDateTime.now();
    private Role role = Role.READER;

    // 독자용 소셜 로그인
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "provider", column = @Column(name = "oauth_provider")),
            @AttributeOverride(name = "oid", column = @Column(name = "oauth_oid"))
    })
    private OAuthInfo oauthInfo;

    // 작가용 아이디/비번
    @Column(name = "login_id")
    private String loginId = null;

    @Column(name = "password")
    private String password = null;

    /** 생성자 로직 **/
    protected User() {}

    @Builder // 독자
    public User(OAuthInfo oauthInfo, String nickName, Gender gender, Set<Genre> favoriteGenreList) {
        this.oauthInfo = oauthInfo;
        this.nickName = nickName;
        this.gender = gender;
        this.favoriteGenreList = favoriteGenreList;
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
    public void withdraw() {
        if (accountState.equals(AccountState.DELETED)) {
            throw AlreadyWithDrawUserException.EXCEPTION;
        }
        accountState = AccountState.DELETED;
        gender = null;
        favoriteGenreList = null;
        profileImageUrl = null;
        if (role.equals(Role.READER)) {
            nickName = "탈퇴한 유저";
            oauthInfo = oauthInfo.withDrawOauthInfo();
        } else {
            nickName = "탈퇴한 작가";
            loginId = null;
            password = null;
        }
    }

}
