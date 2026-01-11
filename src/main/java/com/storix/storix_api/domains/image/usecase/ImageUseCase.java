package com.storix.storix_api.domains.image.usecase;

import com.storix.storix_api.UseCase;
import com.storix.storix_api.domains.image.dto.FileUploadRequest;
import com.storix.storix_api.domains.image.dto.PresignedUrlResponse;
import com.storix.storix_api.domains.image.helper.S3PresignHelper;
import com.storix.storix_api.domains.user.domain.Role;
import com.storix.storix_api.global.apiPayload.CustomResponse;
import com.storix.storix_api.global.apiPayload.code.SuccessCode;
import com.storix.storix_api.global.apiPayload.exception.profile.ProfileImageNotExistException;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class ImageUseCase {

    private final S3PresignHelper s3PresignHelper;

    public CustomResponse<PresignedUrlResponse> getBoardImagePresignedUrl(String role, FileUploadRequest req) {

        if (req.files() != null && !req.files().isEmpty()) {
            for (FileUploadRequest.FileInfo file : req.files()) {
                PresignedUrlResponse result = s3PresignHelper.createPresignedPutUrl(
                        file.contentType(),
                        role.equals(Role.READER.toString()) ? "public/board/reader" : "public/board/artist"
                );
                return CustomResponse.onSuccess(SuccessCode.IMAGE_ISSUE_PRESIGNED_URL_SUCCESS, result);
            }
        }
        return null;
    }

    public CustomResponse<PresignedUrlResponse> getProfileImagePresignedUrl(FileUploadRequest req) {

        if (req.files() != null && !req.files().isEmpty()) {
            if (req.files().size() != 1 ) {
                throw ProfileImageNotExistException.EXCEPTION;
            }

            FileUploadRequest.FileInfo file = req.files().get(0);

            PresignedUrlResponse result = s3PresignHelper.createPresignedPutUrl(
                    file.contentType(),
                    "public/profile"
            );
            return CustomResponse.onSuccess(SuccessCode.IMAGE_ISSUE_PRESIGNED_URL_SUCCESS, result);
        }
        return null;
    }

}
