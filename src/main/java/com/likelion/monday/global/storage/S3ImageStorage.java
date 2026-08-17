package com.likelion.monday.global.storage;

import com.likelion.monday.global.exception.CustomException;
import java.io.IOException;
import java.io.InputStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

/**
 * S3 버킷에 이미지를 저장하는 구현체.
 * 반환하는 URL은 버킷 객체의 공개 주소이므로, 버킷에 읽기 공개 권한이 설정되어 있어야 한다.
 */
@Slf4j
@Component
@ConditionalOnProperty(name = "app.storage.type", havingValue = "s3")
public class S3ImageStorage implements ImageStorage {

    private final S3Client s3Client;
    private final String bucket;
    private final String baseUrl;

    public S3ImageStorage(S3Client s3Client,
                          @Value("${app.storage.s3.bucket}") String bucket,
                          @Value("${app.storage.s3.base-url}") String baseUrl) {
        this.s3Client = s3Client;
        this.bucket = bucket;
        this.baseUrl = baseUrl;
    }

    @Override
    public String upload(MultipartFile image, String directory) {
        String key = directory + "/" + StoredImageName.from(image.getOriginalFilename());

        try (InputStream inputStream = image.getInputStream()) {
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucket)
                            .key(key)
                            .contentType(image.getContentType())
                            .build(),
                    RequestBody.fromInputStream(inputStream, image.getSize()));
        } catch (IOException | SdkException e) {
            log.error("S3 업로드에 실패했습니다. key={}", key, e);
            throw new CustomException(StorageErrorCode.FILE_UPLOAD_FAILED);
        }

        return baseUrl + "/" + key;
    }

    @Override
    public void delete(String imageUrl) {
        String key = extractKey(imageUrl);
        if (key == null) {
            return;
        }

        try {
            s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build());
        } catch (SdkException e) {
            // 삭제 실패가 사연 수정 자체를 막을 이유는 없으므로 경고만 남긴다.
            log.warn("S3 삭제에 실패했습니다. imageUrl={}", imageUrl, e);
        }
    }

    // 이 저장소가 발급한 URL에서만 객체 키를 뽑아낸다.
    private String extractKey(String imageUrl) {
        String prefix = baseUrl + "/";
        if (imageUrl == null || !imageUrl.startsWith(prefix)) {
            return null;
        }

        return imageUrl.substring(prefix.length());
    }
}
