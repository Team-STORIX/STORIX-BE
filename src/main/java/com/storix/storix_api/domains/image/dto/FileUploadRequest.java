package com.storix.storix_api.domains.image.dto;

import java.util.List;

public record FileUploadRequest(
        List<FileInfo> files
) {
    public record FileInfo(String contentType) {}
}
