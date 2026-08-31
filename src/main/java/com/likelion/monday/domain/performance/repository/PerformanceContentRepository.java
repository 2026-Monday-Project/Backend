package com.likelion.monday.domain.performance.repository;

import com.likelion.monday.domain.performance.entity.PerformanceContent;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerformanceContentRepository extends JpaRepository<PerformanceContent, Long> {

    Optional<PerformanceContent> findTopByOrderByIdDesc();
}