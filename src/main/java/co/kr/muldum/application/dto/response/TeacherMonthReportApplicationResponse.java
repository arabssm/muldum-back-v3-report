package co.kr.muldum.application.dto.response;

import co.kr.muldum.domain.model.ReportStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class TeacherMonthReportApplicationResponse {
    private Long reportId;
    private UUID userId;
    private Long teamId; // Added teamId
    private String name; // Added name
    private String topic;
    private String goal;
    private String tech;
    private String problem;
    private String teacherFeedback;
    private String mentorFeedback;
    private ReportStatus status;
    private LocalDateTime submittedAt;
    private Integer score;
}
