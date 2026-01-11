package com.storix.storix_api.domains.plus.controller.dto;

import jakarta.validation.constraints.Size;

import java.util.List;

public record ReaderBoardUploadRequest(

        boolean isWorksSelected,
        Long worksId,

        boolean isSpoiler,

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
