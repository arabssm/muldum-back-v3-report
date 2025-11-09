package co.kr.muldum.presentation.web.dto.response;

import co.kr.muldum.domain.model.ReportStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class TeacherMonthReportSimpleResponse {
    private Long teamId;
    private String name;
    private String topic;
    private Long reportId;
    private ReportStatus status;
    private LocalDateTime submitedAt;
}
