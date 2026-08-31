package com.likelion.monday.domain.performance.entity;

import com.likelion.monday.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "performance_content")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PerformanceContent extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime contentOpenAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PerformanceContentStatus contentStatus = PerformanceContentStatus.COMING_SOON;
}
