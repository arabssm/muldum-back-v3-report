package co.kr.muldum.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID; // Import UUID

@Getter
@Builder
@RequiredArgsConstructor
public class MonthReport {

    private final Long id;
    private final UUID userId; // Changed from Long to UUID
    private final String topic;
    private final String goal;
    private final String tech;
    private final String problem;
    private final String teacherFeedback;
    private final String mentorFeedback;
    private final ReportStatus status;
    private final LocalDateTime submittedAt;
    private final int score;
    private final LocalDateTime createdAt;
}
