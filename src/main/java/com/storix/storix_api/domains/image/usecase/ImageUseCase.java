package com.storix.storix_api.domains.image.usecase;

import com.storix.storix_api.UseCase;
import com.storix.storix_api.domains.image.dto.FileUploadRequest;
import com.storix.storix_api.domains.image.dto.PresignedUrlResponse;
import com.storix.storix_api.domains.image.dto.ProfileImageUploadRequest;
import com.storix.storix_api.domains.image.helper.S3PresignHelper;
import com.storix.storix_api.domains.user.domain.Role;
import com.storix.storix_api.global.apiPayload.CustomResponse;
import com.storix.storix_api.global.apiPayload.code.SuccessCode;
import lombok.RequiredArgsConstructor;

import java.util.List;

@UseCase
@RequiredArgsConstructor
public class ImageUseCase {

    private final S3PresignHelper s3PresignHelper;

    public CustomResponse<List<PresignedUrlResponse>> getBoardImagePresignedUrl(Role role, FileUploadRequest req) {

        String prefix = Role.READER.equals(role)
                ? "public/board/reader" : "public/board/artist";

        List<PresignedUrlResponse> results = req.files().stream()
                .map(file -> s3PresignHelper.createPresignedPutUrl(file.contentType(), prefix))
                .toList();

        return CustomResponse.onSuccess(SuccessCode.IMAGE_ISSUE_PRESIGNED_URL_SUCCESS, results);
    }

    public CustomResponse<PresignedUrlResponse> getProfileImagePresignedUrl(ProfileImageUploadRequest req) {

        PresignedUrlResponse result = s3PresignHelper.createPresignedPutUrl(
                req.file().contentType(),
                "public/profile"
        );
        return CustomResponse.onSuccess(SuccessCode.IMAGE_ISSUE_PRESIGNED_URL_SUCCESS, result);
    }

    public CustomResponse<String> getImageUrl(String objectKey) {
        String presignedGetUrl = s3PresignHelper.createPresignedGetUrl(objectKey);
        return CustomResponse.onSuccess(SuccessCode.SUCCESS, presignedGetUrl);
    }

    public CustomResponse<List<PresignedUrlResponse>> getFanBoardImagePresignedUrl(FileUploadRequest req) {

        String prefix = "private/board/artist";

        List<PresignedUrlResponse> results = req.files().stream()
                .map(file -> s3PresignHelper.createPresignedPutUrl(file.contentType(), prefix))
                .toList();

        return CustomResponse.onSuccess(SuccessCode.IMAGE_ISSUE_PRESIGNED_URL_SUCCESS, results);
    }

}
