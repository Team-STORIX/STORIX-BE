package com.storix.storix_api.domains.plus.adaptor;

import com.storix.storix_api.domains.plus.domain.ArtistBoard;
import com.storix.storix_api.domains.plus.domain.ArtistBoardImage;
import com.storix.storix_api.domains.plus.domain.ReaderBoard;
import com.storix.storix_api.domains.plus.domain.ReaderBoardImage;
import com.storix.storix_api.domains.plus.repository.ArtistBoardImageRepository;
import com.storix.storix_api.domains.plus.repository.ReaderBoardImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BoardImageAdaptor {

    public final ReaderBoardImageRepository readerBoardImageRepository;
    public final ArtistBoardImageRepository artistBoardImageRepository;

    public void saveReaderBoardImages(ReaderBoard readerBoard, List<String> objectKeys) {

        List<ReaderBoardImage> images = new ArrayList<>();

        for (int i = 0; i < objectKeys.size(); i++) {
            images.add(
                    ReaderBoardImage.of(
                            readerBoard,
                            objectKeys.get(i),
                            i // sortOrder
                    )
            );
        }

        readerBoardImageRepository.saveAll(images);
    }

    public void saveArtistBoardImages(ArtistBoard artistBoard, List<String> objectKeys) {
        if (objectKeys == null || objectKeys.isEmpty()) return;

        List<ArtistBoardImage> images = new ArrayList<>();

        for (int i = 0; i < objectKeys.size(); i++) {
            images.add(
                    ArtistBoardImage.of(
                            artistBoard,
                            objectKeys.get(i),
                            i // sortOrder
                    )
            );
        }

        artistBoardImageRepository.saveAll(images);
    }

}
