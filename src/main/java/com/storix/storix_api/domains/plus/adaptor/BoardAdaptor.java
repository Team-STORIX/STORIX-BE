package com.storix.storix_api.domains.plus.adaptor;

import com.storix.storix_api.domains.plus.domain.ArtistBoard;
import com.storix.storix_api.domains.plus.domain.ReaderBoard;
import com.storix.storix_api.domains.plus.dto.CreateArtistBoardCommand;
import com.storix.storix_api.domains.plus.dto.CreateReaderBoardCommand;
import com.storix.storix_api.domains.plus.repository.ArtistBoardRepository;
import com.storix.storix_api.domains.plus.repository.ReaderBoardRepository;
import com.storix.storix_api.global.apiPayload.exception.plus.DuplicateBoardUploadException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BoardAdaptor {

    private final ReaderBoardRepository readerBoardRepository;
    private final ArtistBoardRepository artistBoardRepository;

    /**
     * 독자
     * */
    // 독자 게시글 생성
    public ReaderBoard saveReaderBoard(CreateReaderBoardCommand cmd) {
        try {
            ReaderBoard readerBoard = cmd.toEntity();
            readerBoard.replaceImages(cmd.objectKeys());
            return readerBoardRepository.save(readerBoard);
        } catch (DataIntegrityViolationException e) {
            throw DuplicateBoardUploadException.EXCEPTION;
        }
    }

    /**
     * 작가
     * */
    // 작가 게시글 생성
    public ArtistBoard saveArtistBoard(CreateArtistBoardCommand cmd) {
        try {
            ArtistBoard artistBoard = cmd.toEntity();
            artistBoard.replaceImages(cmd.objectKeys());
            return artistBoardRepository.save(cmd.toEntity());
        } catch (DataIntegrityViolationException e) {
            throw DuplicateBoardUploadException.EXCEPTION;
        }
    }


}
