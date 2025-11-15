package co.kr.muldum.presentation.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SaveMonthReportResponse {
    private Long reportId;
    private String message;
}
