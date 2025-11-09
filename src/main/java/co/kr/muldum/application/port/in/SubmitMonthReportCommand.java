package co.kr.muldum.application.port.in;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID; // Import UUID

@Getter
@RequiredArgsConstructor
public class SubmitMonthReportCommand {
    private final UUID userId; // Changed from Long to UUID
    private final Long reportId;
    private final String topic;
    private final String goal;
    private final String tech;
    private final String problem;
    private final String teacherFeedback;
    private final String mentorFeedback;
}
