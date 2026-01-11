package com.storix.storix_api.domains.image.helper;

import com.storix.storix_api.domains.image.dto.PresignedUrlResponse;
import com.storix.storix_api.global.apiPayload.exception.plus.InvalidContentTypeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3PresignHelper {

    private final S3Presigner presigner;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    public static String nowAsKeyPart() {
        return LocalDateTime.now().format(FORMATTER);
    }

    // 공개 컨텐츠 용 (프로필, 게시글, 리뷰)
    public PresignedUrlResponse createPresignedPutUrl(String contentType, String objectKeyPrefix) {

        String ext = contentTypeToExt(contentType);
        String objectKey = objectKeyPrefix + "/" + UUID.randomUUID() + "." + ext;

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .contentType(contentType)
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(5))
                .putObjectRequest(putObjectRequest)
                .build();

        PresignedPutObjectRequest presignedRequest = presigner.presignPutObject(presignRequest);

        log.info("Presigned URL to upload a file to: [{}]", presignedRequest.url().toString());
        log.info("PUT HTTP method: [{}]", presignedRequest.httpRequest().method());

        return new PresignedUrlResponse(
                presignedRequest.url().toString(),
                objectKey,
                presignRequest.signatureDuration().toSeconds()
        );
    }

    // 작가 팬 콘텐츠 조회용
    public String createPresignedGetUrl(String bucketName, String keyName) {

        GetObjectRequest objectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(keyName)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10)) // 피드 캐싱 시간과 동일하게 업데이트할 에정
                .getObjectRequest(objectRequest)
                .build();

        PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(presignRequest);

        log.info("Presigned URL to download a file to: [{}]", presignedRequest.url().toString());
        log.info("HTTP method: [{}]", presignedRequest.httpRequest().method());

        return presignedRequest.url().toExternalForm();
    }

    // 이미지 다운로드 용
    public PresignedUrlResponse createPresignedDownloadUrl(String objectKey, String contentType) {

        String ext = contentTypeToExt(contentType);
        String downloadFileName = nowAsKeyPart() + "." + ext;

        GetObjectRequest getReq = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .responseContentDisposition(downloadFileName)
                .responseContentType(contentType)
                .build();

        GetObjectPresignRequest presignReq = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10))
                .getObjectRequest(getReq)
                .build();

        PresignedGetObjectRequest presigned = presigner.presignGetObject(presignReq);

        return new PresignedUrlResponse(
                presigned.url().toString(),
                objectKey,
                presignReq.signatureDuration().toSeconds()
        );
    }

    // ContentType
    private String contentTypeToExt(String contentType) {
        return switch (contentType) {
            case "image/jpeg" -> "jpg";
            case "image/png" -> "png";
            case "image/webp" -> "webp";
            default -> throw InvalidContentTypeException.EXCEPTION;
        };
    }

}
