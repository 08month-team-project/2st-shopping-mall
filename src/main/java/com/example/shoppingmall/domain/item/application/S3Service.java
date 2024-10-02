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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import static com.example.shoppingmall.global.exception.ErrorCode.IMAGE_TOO_LARGE;
import static com.example.shoppingmall.global.exception.ErrorCode.INVALID_IMAGE_TYPE;

@Service
@RequiredArgsConstructor
public class S3Service {

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;
    private final S3Presigner s3Presigner;
    private final S3Client s3Client;

    public List<String> createUrlsForUpload(List<MultipartFile> files) {
        return files.stream()
                .map(this::uploadValidation)
                .collect(Collectors.toList());
    }

    public void urlForUpdate(MultipartFile oldFile, MultipartFile newFile) throws MalformedURLException {
        urlForDelete(oldFile);
        createUrlsForUpload(Collections.singletonList(newFile));
    }

    public void urlForDelete(MultipartFile file) throws MalformedURLException {
        String imagePath = extractKeyFromUrl(file.getOriginalFilename());
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(imagePath)
                .build();
        s3Client.deleteObject(deleteObjectRequest);
    }

    private String uploadValidation(MultipartFile file) {
        // 1. 파일 이름이 동일할 경우 UUID를 통해 고유 키를 나눠준다.
        String uuid = UUID.randomUUID().toString();
        String uniqueFileName = uuid + "_" + file.getOriginalFilename();

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
        List<String> allowedExtensions = Arrays.asList("jpg", "jpeg", "png");

        if (originalFileName != null) {
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1).toLowerCase();

            if (!allowedExtensions.contains(fileExtension)) {
                throw new S3Exception(INVALID_IMAGE_TYPE);
            }
        }
    }

    private void checkFileSize(long fileSize) {
        // 1MB
        long maxFileSize = 1_000_000;
        if (fileSize > maxFileSize) {
            throw new S3Exception(IMAGE_TOO_LARGE);
        }
    }

    private String extractKeyFromUrl(String url) throws MalformedURLException {
        return new URL(url).getPath().substring(1); // URL에서 '/'를 제거하여 키를 추출
    }
}
