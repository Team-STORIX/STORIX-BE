package com.storix.storix_api.domains.works.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "works")
@Getter
public class Works {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "works_id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    // 작품 플랫폼
    @Column(nullable = false)
    private Platform platform;

    // 작품명
    @Column(name = "works_name")
    private String worksName;

    // 전체 작가
    @Column(name = "artist_name")
    private String artistName;

    @Column(length = 100)
    private String author;

    @Column(length = 100)
    private String illustrator;

    @Column(name = "original_author", length = 100)
    private String originalAuthor;

    // 연령 등급
    @Column(name = "age_classification", nullable = false)
    private AgeClassification ageClassification;

    // 작품 소개
    @Lob
    @Column(nullable = false)
    private String description;

    // 작품 장르
    @Column(nullable = false)
    private Genre genre;

    @Column(name = "thumbnail_url", nullable = false)
    private String thumbnailUrl;

    @Column(name = "reviews_count")
    private Integer reviewsCount;

    @Column(name = "avg_rating")
    private Float avgRating;

    @Column(name = "works_type", nullable = false)
    private WorksType worksType;
}
