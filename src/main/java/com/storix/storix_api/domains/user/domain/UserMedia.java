package com.storix.storix_api.domains.user.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@Getter @RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class UserMedia {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profileMediaId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    private String imagePath;

    @Builder
    public UserMedia(User user, String imagePath) {
        this.user = user;
        this.imagePath = imagePath;
    }
}
