package com.example.shoppingmall.domain.item.application;

import com.example.shoppingmall.domain.item.excepction.S3Exception;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.shoppingmall.global.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class S3Service {

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;
    private final S3Presigner s3Presigner;
    private final S3Client s3Client;
    private final List<String> allowedExtensions = Arrays.asList("jpg", "jpeg", "png");
    private final long maxFileSize = 1_000_000;

    public List<String> createUrlsForUpload(List<MultipartFile> files) {

        // 파일 수를 체크
        if (files.size() > 3) throw new S3Exception(MAX_UPLOAD_LIMIT);

        return files.stream()
                .map(this::uploadValidation)
                .collect(Collectors.toList());
    }

    public void urlForUpdate(MultipartFile oldFile, MultipartFile newFile) {
        urlForDelete(oldFile);
        createUrlsForUpload(Collections.singletonList(newFile));
    }

    // 이미지 삭제를 위한 메서드
    public void urlForDelete(MultipartFile file) {
        Optional.ofNullable(file.getOriginalFilename())
                .map(this::extractKeyFromUrl) // URL에서 키를 추출
                .ifPresentOrElse(imagePath -> {
                    // 경로가 존재하는 경우
                    DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                            .bucket(bucket)
                            .key(imagePath)
                            .build();
                    s3Client.deleteObject(deleteObjectRequest);
                }, () -> { throw new S3Exception(NOT_VALID_URL); });
    }

    private String uploadValidation(MultipartFile file) {

        // 1. 파일 이름이 동일할 경우를 대비해서 UUID 에서 생성된 키와 함께 S3에 저장
        String uniqueFileName = Optional.of(file)
                .filter(f -> !f.isEmpty())
                .map(f -> {
                    String uuid = UUID.randomUUID().toString();
                    return uuid + "_" + f.getOriginalFilename();
                })
                .orElseThrow(() -> new S3Exception(NOT_ENOUGH_IMAGES));

        // 2. 이미지 확장자 확인
        checkImageExtension(file.getOriginalFilename());

        // 3. 이미지 크기 확인 = 1MB
        checkFileSize(file.getSize());

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key("test/" + uniqueFileName)
                .build();

        PutObjectPresignRequest preSignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(3))
                .putObjectRequest(putObjectRequest)
                .build();

        return s3Presigner.presignPutObject(preSignRequest).url().toString();
    }

    private void checkImageExtension(String originalFileName) {
        Optional.ofNullable(originalFileName)
                .filter(f -> !f.isEmpty())
                .map(f -> f.substring(f.lastIndexOf(".") + 1).toLowerCase()) // 확장자 추출
                .filter(allowedExtensions::contains) // 허용된 확장자인지 검사
                .orElseThrow(() -> new S3Exception(INVALID_IMAGE_TYPE));
    }

    private void checkFileSize(long fileSize) {
        Optional.of(fileSize)
                .filter((f) -> f < maxFileSize)
                .orElseThrow(() -> new S3Exception(IMAGE_TOO_LARGE));
    }

    private String extractKeyFromUrl(String url) {
        return Optional.ofNullable(url) // url이 null이 아닐 경우 Optional 생성
                .filter(this::isValidUrl) // URL이 유효한 경우만 필터링
                .map(this::getPathFromUrl)
                .orElseThrow(() -> new S3Exception(NOT_FOUND_IMAGE_URL));
    }

    private boolean isValidUrl(String url) {
        // URL 형식 검증
        return url.matches("^(http|https)://.*$");
    }

    // 유효한 URL에서 경로를 추출하는 메서드
    private String getPathFromUrl(String url) {
        try {
            return new URL(url).getPath().substring(1); // URL에서 '/'를 제거하여 키를 추출
        } catch (MalformedURLException e) {
            throw new S3Exception(INVALID_URL_FORMAT); // 잘못된 URL 형식일 경우 예외 발생
        }
    }

}
