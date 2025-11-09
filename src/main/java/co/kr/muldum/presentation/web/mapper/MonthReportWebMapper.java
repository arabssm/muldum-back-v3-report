package co.kr.muldum.presentation.web.mapper;

import co.kr.muldum.application.port.in.SaveMonthReportCommand;
import co.kr.muldum.application.port.in.SubmitMonthReportCommand;
import co.kr.muldum.domain.model.MonthReport;
import co.kr.muldum.presentation.web.dto.request.MonthReportRequest;
import co.kr.muldum.presentation.web.dto.response.MonthReportDetailResponse;
import co.kr.muldum.presentation.web.dto.response.MonthReportSimpleResponse;
import org.springframework.stereotype.Component;

import java.util.UUID; // Import UUID

@Component
public class MonthReportWebMapper {

    public SaveMonthReportCommand toCommand(MonthReportRequest request, UUID userId) { // Changed from Long to UUID
        return new SaveMonthReportCommand(
                userId,
                request.getTopic(),
                request.getGoal(),
                request.getTech(),
                request.getProblem(),
                request.getTeacherFeedback(),
                request.getMentorFeedback()
        );
    }

    public SubmitMonthReportCommand toCommand(MonthReportRequest request, UUID userId, Long reportId) { // Changed from Long to UUID
        return new SubmitMonthReportCommand(
                userId,
                reportId,
                request.getTopic(),
                request.getGoal(),
                request.getTech(),
                request.getProblem(),
                request.getTeacherFeedback(),
                request.getMentorFeedback()
        );
    }

    public MonthReportDetailResponse toDetailResponse(MonthReport report) {
        return MonthReportDetailResponse.builder()
                .reportId(report.getId())
                .topic(report.getTopic())
                .goal(report.getGoal())
                .tech(report.getTech())
                .problem(report.getProblem())
                .teacherFeedback(report.getTeacherFeedback())
                .mentorFeedback(report.getMentorFeedback())
                .status(report.getStatus())
                .build();
    }

    public MonthReportSimpleResponse toSimpleResponse(MonthReport report) {
        return MonthReportSimpleResponse.builder()
                .reportId(report.getId())
                .topic(report.getTopic())
                .status(report.getStatus())
                .submitedAt(report.getSubmittedAt())
                .build();
    }
}
