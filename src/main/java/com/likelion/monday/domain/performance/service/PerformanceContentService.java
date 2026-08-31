package com.likelion.monday.domain.performance.service;

import com.likelion.monday.domain.performance.dto.PerformanceContentResDto;
import com.likelion.monday.domain.performance.entity.PerformanceContent;
import com.likelion.monday.domain.performance.entity.PerformanceContentStatus;
import com.likelion.monday.domain.performance.exception.PerformanceErrorCode;
import com.likelion.monday.domain.performance.repository.PerformanceContentRepository;
import com.likelion.monday.global.exception.CustomException;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PerformanceContentService {

    private final PerformanceContentRepository performanceContentRepository;

    /**
     * 저장된 contentStatus를 그대로 믿지 않고, 현재 시각과 contentOpenAt을 비교해 공개 상태를 다시 계산한다.
     */
    public PerformanceContentResDto getPerformanceContent() {
        PerformanceContent content = performanceContentRepository.findTopByOrderByCreatedAtDesc()
                .orElseThrow(() -> new CustomException(PerformanceErrorCode.PERFORMANCE_CONTENT_NOT_FOUND));

        PerformanceContentStatus status = LocalDateTime.now().isBefore(content.getContentOpenAt())
                ? PerformanceContentStatus.COMING_SOON
                : PerformanceContentStatus.OPEN;

        return new PerformanceContentResDto(status, content.getContentOpenAt());
    }
}