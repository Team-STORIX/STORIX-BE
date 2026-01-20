package com.storix.storix_api.domains.onboarding.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OnboardingWorks {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "works_id", nullable = false)
    private Long worksId;

    @Column(nullable = false)
    private String title;

    @Column(name = "artist_name", nullable = false)
    private String artistName;

    @Column(name = "thumbnail_url", nullable = false)
    private String thumbnailUrl;

    @Builder
    public OnboardingWorks(Long worksId, String title, String artistName, String thumbnailUrl) {
        this.worksId = worksId;
        this.title = title;
        this.artistName = artistName;
        this.thumbnailUrl = thumbnailUrl;
    }

}
