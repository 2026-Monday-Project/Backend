package com.likelion.monday.global.storage;

import com.likelion.monday.global.exception.CustomException;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * 저장소에 실제로 기록될 파일명을 만든다.
 * 원본 파일명을 그대로 쓰면 한글·공백·중복 때문에 문제가 생기므로 UUID로 바꾸고 확장자만 남긴다.
 */
final class StoredImageName {

    private static final List<String> ALLOWED_EXTENSIONS = List.of("jpg", "jpeg", "png", "gif", "webp", "heic");

    private StoredImageName() {
    }

    static String from(String originalFileName) {
        if (originalFileName == null || !originalFileName.contains(".")) {
            throw new CustomException(StorageErrorCode.UNSUPPORTED_FILE_EXTENSION);
        }

        String extension = originalFileName
                .substring(originalFileName.lastIndexOf(".") + 1)
                .toLowerCase(Locale.ROOT);
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new CustomException(StorageErrorCode.UNSUPPORTED_FILE_EXTENSION);
        }

        return UUID.randomUUID() + "." + extension;
    }
}
