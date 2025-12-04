package com.storix.storix_api.domains.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class OAuthInfo {

    @Enumerated(EnumType.STRING)
    @Column(name = "oauth_provider")
    private OAuthProvider provider;

    @Column(name = "oauth_oid")
    private String oid;

    @Builder
    public OAuthInfo(OAuthProvider provider, String oid) {
        this.provider = provider;
        this.oid = oid;
    }

    // 회원 탈퇴 시
}
