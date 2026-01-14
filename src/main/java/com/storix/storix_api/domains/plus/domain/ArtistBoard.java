package com.storix.storix_api.domains.plus.domain;

import com.storix.storix_api.global.model.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "artist_board")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArtistBoard extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "artist_board_id")
    private Long id;

    // 게시글 작성 작가
    @Column(name = "user_id")
    private Long userId;

    // 게시글 작품 정보
    @Column(name = "is_works_selected")
    private boolean isWorksSelected;

    @Column(name = "works_id")
    private Long worksId;

    // 게시글 내용 정보
    @Column(name = "is_content_for_fan")
    private boolean isContentForFan;

    @Column(name = "point")
    private Integer point;

    @Column(length = 300, nullable = false)
    private String content;

    @OneToMany(mappedBy = "artistBoard", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sortOrder ASC")
    private List<ArtistBoardImage> images = new ArrayList<>();

    // 게시글 반응
    @Column(name = "like_count", nullable = false)
    private int likeCount = 0;

    @Column(nullable = false, name = "reply_count")
    private int replyCount = 0;

    /** 생성자 로직 **/
    @Builder
    public ArtistBoard(Long userId, boolean isWorksSelected, Long worksId, boolean isContentForFan, Integer point, String content) {
        this.userId = userId;
        this.isWorksSelected = isWorksSelected;
        this.worksId = worksId;
        this.isContentForFan = isContentForFan;
        this.point = point;
        this.content = content;
    }

    /** 비즈니스 로직 **/
    public void updateLikeCount() {
        this.likeCount ++;
    }

    public void updateReplyCount() {
        this.replyCount ++;
    }

}