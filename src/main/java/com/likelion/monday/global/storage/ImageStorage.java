package com.likelion.monday.global.storage;

import org.springframework.web.multipart.MultipartFile;

/**
 * 이미지 저장소 추상화.
 * 현재는 로컬 디스크(LocalImageStorage)에 저장하고, 배포 시 S3 구현체로 교체한다.
 */
public interface ImageStorage {

    /**
     * 이미지를 저장하고 외부에서 접근 가능한 URL을 돌려준다.
     *
     * @param image     저장할 이미지 파일
     * @param directory 저장소 내부에서 구분할 디렉터리명 (예: "story")
     */
    String upload(MultipartFile image, String directory);

    // upload가 돌려준 URL을 그대로 받아 해당 이미지를 삭제한다. 이미 없는 이미지는 무시한다.
    void delete(String imageUrl);
}
