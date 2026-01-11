package com.storix.storix_api.domains.plus.controller.dto;

import jakarta.validation.constraints.Size;

import java.util.List;

public record ArtistBoardUploadRequest(

        boolean isWorksSelected,
        Long worksId,

        boolean isContentForFan,
        @Size(min = 100, message = "point는 100 이상이어야 합니다.")
        Integer point,

        @Size(max = 300, message = "게시글은 300자까지 가능합니다.")
        String content,

        List<FileKeys> files
) {
    public record FileKeys(String objectKey) {}

    public List<String> objectKeys() {
        if (files == null) return List.of();
        return files.stream()
                .map(FileKeys::objectKey)
                .filter(k -> k != null && !k.isBlank())
                .toList();
    }
}
