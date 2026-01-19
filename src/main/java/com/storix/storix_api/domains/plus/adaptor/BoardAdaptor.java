package com.storix.storix_api.domains.plus.adaptor;

import com.storix.storix_api.domains.plus.domain.ArtistBoard;
import com.storix.storix_api.domains.plus.domain.ReaderBoard;
import com.storix.storix_api.domains.plus.dto.CreateArtistBoardCommand;
import com.storix.storix_api.domains.plus.dto.CreateReaderBoardCommand;
import com.storix.storix_api.domains.plus.dto.ReaderBoardInfo;
import com.storix.storix_api.domains.plus.repository.ArtistBoardRepository;
import com.storix.storix_api.domains.plus.repository.ReaderBoardRepository;
import com.storix.storix_api.global.apiPayload.exception.plus.DuplicateBoardUploadException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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
            return readerBoardRepository.save(readerBoard);
        } catch (DataIntegrityViolationException e) {
            throw DuplicateBoardUploadException.EXCEPTION;
        }
    }

    // 독자 내 게시글 조회
    public Slice<ReaderBoardInfo> findAllReaderBoardList(Long userId, Pageable pageable) {
        Slice<ReaderBoard> result =
                readerBoardRepository.findAllReaderBoardByUserId(userId, pageable);
        return result.map(ReaderBoardInfo::ofMyBoard);
    }

    /**
     * 작가
     * */
    // 작가 게시글 생성
    public ArtistBoard saveArtistBoard(CreateArtistBoardCommand cmd) {
        try {
            ArtistBoard artistBoard = cmd.toEntity();
            return artistBoardRepository.save(cmd.toEntity());
        } catch (DataIntegrityViolationException e) {
            throw DuplicateBoardUploadException.EXCEPTION;
        }
    }


}
