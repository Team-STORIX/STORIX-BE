package com.storix.storix_api.domains.plus.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(
        name = "reader_board",
        indexes = {
                @Index(name = "idx_reader_board_user_id", columnList = "user_id")
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReaderBoard extends Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reader_board_id")
    private Long id;

    @Column(name = "is_spoiler")
    private boolean isSpoiler;


    @Builder
    public ReaderBoard(Long userId, boolean isWorksSelected,
                       Long worksId, boolean isSpoiler, String content) {
        this.userId = userId;
        this.isWorksSelected = isWorksSelected;
        this.worksId = worksId;
        this.isSpoiler = isSpoiler;
        this.content = content;
    }
}