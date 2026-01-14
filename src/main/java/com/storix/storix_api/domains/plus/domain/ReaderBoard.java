package com.storix.storix_api.domains.plus.domain;

import com.storix.storix_api.global.model.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reader_board")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReaderBoard extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reader_board_id")
    private Long id;

    // 게시글 작성 유저
    @Column(name = "user_id", nullable = false)
    private Long userId;

    // 게시글 작품 정보
    @Column(name = "is_works_selected")
    private boolean isWorksSelected;

    @Column(name = "works_id")
    private Long worksId;

    // 게시글 내용 정보
    @Column(name = "is_spoiler")
    private boolean isSpoiler;

    @Column(length = 300, nullable = false)
    private String content;

    // 게시글 반응
    @Column(name = "like_count", nullable = false)
    private int likeCount = 0;

    @Column(name = "reply_count", nullable = false)
    private int replyCount = 0;


    /** 생성자 로직 **/
    @Builder
    public ReaderBoard(Long userId, boolean isWorksSelected, Long worksId, boolean isSpoiler, String content) {
        this.userId = userId;
        this.isWorksSelected = isWorksSelected;
        this.worksId = worksId;
        this.isSpoiler = isSpoiler;
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
