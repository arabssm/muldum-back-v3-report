package co.kr.muldum.presentation.web.dto.request;

import lombok.Getter;

@Getter
public class MonthReportRequest {
    private String topic;
    private String goal;
    private String tech;
    private String problem;
    private String teacherFeedback;
    private String mentorFeedback;
}
