package co.kr.muldum.presentation.web.dto.request;

import lombok.Getter;

@Getter
public class FeedbackRequest {
    private Long teamId;
    private String name;
    private Long reportId;
    private String feedback;
}
