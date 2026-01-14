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

    private final ReaderBoardImageRepository readerBoardImageRepository;
    private final ArtistBoardImageRepository artistBoardImageRepository;

    public void saveReaderBoardImages(ReaderBoard readerBoard, List<String> objectKeys) {
        List<ReaderBoardImage> images = buildImages(objectKeys,
                (objectKey, sortOrder) -> ReaderBoardImage.of(readerBoard, objectKey, sortOrder)
        );

        readerBoardImageRepository.saveAll(images);
    }

    public void saveArtistBoardImages(ArtistBoard artistBoard, List<String> objectKeys) {
        List<ArtistBoardImage> images = buildImages(objectKeys,
                (objectKey, sortOrder) -> ArtistBoardImage.of(artistBoard, objectKey, sortOrder)
        );

        artistBoardImageRepository.saveAll(images);
    }

    private <T> List<T> buildImages(List<String> objectKeys, ImageFactory<T> factory) {
        if (objectKeys == null || objectKeys.isEmpty()) return List.of();

        List<T> images = new ArrayList<>(objectKeys.size());
        for (int i = 0; i < objectKeys.size(); i++) {
            String key = objectKeys.get(i);
            images.add(factory.create(key, i));
        }
        return images;
    }

    @FunctionalInterface
    private interface ImageFactory<T> {
        T create(String objectKey, int sortOrder);
    }
}