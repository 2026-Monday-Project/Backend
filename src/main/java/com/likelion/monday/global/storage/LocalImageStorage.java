package com.likelion.monday.global.storage;

import com.likelion.monday.global.exception.CustomException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * 로컬 디스크에 이미지를 저장하는 구현체.
 * 저장 위치는 app.storage.local.root-dir, 노출 URL 접두어는 app.storage.local.base-url로 설정한다.
 */
@Slf4j
@Component
public class LocalImageStorage implements ImageStorage {

    private static final List<String> ALLOWED_EXTENSIONS = List.of("jpg", "jpeg", "png", "gif", "webp", "heic");

    private final Path rootDir;
    private final String baseUrl;

    public LocalImageStorage(@Value("${app.storage.local.root-dir:./uploads}") String rootDir,
                             @Value("${app.storage.local.base-url:/images}") String baseUrl) {
        this.rootDir = Paths.get(rootDir).toAbsolutePath().normalize();
        this.baseUrl = baseUrl;
    }

    @Override
    public String upload(MultipartFile image, String directory) {
        String extension = extractExtension(image.getOriginalFilename());
        String fileName = UUID.randomUUID() + "." + extension;
        Path targetDir = rootDir.resolve(directory).normalize();

        try (InputStream inputStream = image.getInputStream()) {
            Files.createDirectories(targetDir);
            Files.copy(inputStream, targetDir.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.error("이미지 저장에 실패했습니다. fileName={}", fileName, e);
            throw new CustomException(StorageErrorCode.FILE_UPLOAD_FAILED);
        }

        return baseUrl + "/" + directory + "/" + fileName;
    }

    @Override
    public void delete(String imageUrl) {
        Path target = resolveStoredPath(imageUrl);
        if (target == null) {
            return;
        }

        try {
            Files.deleteIfExists(target);
        } catch (IOException e) {
            // 파일 삭제 실패가 사연 수정 자체를 막을 이유는 없으므로 경고만 남긴다.
            log.warn("이미지 삭제에 실패했습니다. imageUrl={}", imageUrl, e);
        }
    }

    private String extractExtension(String originalFileName) {
        if (originalFileName == null || !originalFileName.contains(".")) {
            throw new CustomException(StorageErrorCode.UNSUPPORTED_FILE_EXTENSION);
        }

        String extension = originalFileName
                .substring(originalFileName.lastIndexOf(".") + 1)
                .toLowerCase(Locale.ROOT);
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new CustomException(StorageErrorCode.UNSUPPORTED_FILE_EXTENSION);
        }

        return extension;
    }

    /**
     * 저장소가 발급한 URL만 실제 경로로 되돌린다.
     * 경로 조작으로 저장소 밖 파일이 지워지지 않도록 rootDir 하위인지 다시 확인한다.
     */
    private Path resolveStoredPath(String imageUrl) {
        String prefix = baseUrl + "/";
        if (imageUrl == null || !imageUrl.startsWith(prefix)) {
            return null;
        }

        Path target = rootDir.resolve(imageUrl.substring(prefix.length())).normalize();
        if (!target.startsWith(rootDir)) {
            return null;
        }

        return target;
    }
}
