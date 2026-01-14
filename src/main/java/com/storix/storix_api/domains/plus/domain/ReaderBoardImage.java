package com.storix.storix_api.domains.plus.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "reader_board_image")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReaderBoardImage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reader_board_image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reader_board_id")
    private ReaderBoard readerBoard;

    @Column(name = "image_object_key", nullable = false)
    private String imageObjectKey;

    @Column(name = "sort_order", nullable = false)
    private int sortOrder;


    /** 생성자 로직 **/
    @Builder
    public ReaderBoardImage(ReaderBoard readerBoard, String imageObjectKey, int sortOrder) {
        this.readerBoard = readerBoard;
        this.imageObjectKey = imageObjectKey;
        this.sortOrder = sortOrder;
    }

    public static ReaderBoardImage of(ReaderBoard readerBoard, String objectKey, int sortOrder) {
        return new ReaderBoardImage(readerBoard, objectKey, sortOrder);
    }

}
