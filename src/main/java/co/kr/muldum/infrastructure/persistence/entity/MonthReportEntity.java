package co.kr.muldum.infrastructure.persistence.entity;

import co.kr.muldum.domain.model.ReportStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode; // Import for JdbcTypeCode
import org.hibernate.type.SqlTypes; // Import for SqlTypes

@Entity
@Table(name = "month_report")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MonthReportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @JdbcTypeCode(SqlTypes.JSON) // Annotation to store as JSONB
    @Column(name = "report_content", columnDefinition = "jsonb") // Explicitly define column type for PostgreSQL
    private ReportContent reportContent; // Consolidated fields

    @Enumerated(EnumType.STRING)
    private ReportStatus status;

    private LocalDateTime submittedAt;

    private int score;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
