package com.storix.storix_api.domains.plus.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "artist_board_image")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArtistBoardImage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "artist_board_image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "artist_board_id", nullable = false)
    private ArtistBoard artistBoard;

    @Column(name = "image_object_key", nullable = false)
    private String imageObjectKey;

    @Column(name = "sort_order", nullable = false)
    private int sortOrder;


    /** 생성자 로직 **/
    @Builder
    public ArtistBoardImage(ArtistBoard artistBoard, String imageObjectKey, int sortOrder) {
        this.artistBoard = artistBoard;
        this.imageObjectKey = imageObjectKey;
        this.sortOrder = sortOrder;
    }

    public static ArtistBoardImage of(ArtistBoard artistBoard, String objectKey, int sortOrder) {
        return new ArtistBoardImage(artistBoard, objectKey, sortOrder);
    }

    public void changeOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

}
