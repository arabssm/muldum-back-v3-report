package co.kr.muldum.application.port.in;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SubmitMonthReportCommand {
    private final Long userId;
    private final String topic;
    private final String goal;
    private final String tech;
    private final String problem;
    private final String teacherFeedback;
    private final String mentorFeedback;
}
