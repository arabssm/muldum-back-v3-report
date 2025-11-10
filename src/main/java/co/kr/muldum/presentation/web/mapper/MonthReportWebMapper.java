package co.kr.muldum.presentation.web.mapper;

import co.kr.muldum.application.dto.response.TeacherMonthReportApplicationResponse;
import co.kr.muldum.application.port.in.SaveMonthReportCommand;
import co.kr.muldum.application.port.in.SubmitMonthReportCommand;
import co.kr.muldum.domain.model.MonthReport;
import co.kr.muldum.presentation.web.dto.request.MonthReportRequest;
import co.kr.muldum.presentation.web.dto.response.MonthReportDetailResponse;
import co.kr.muldum.presentation.web.dto.response.MonthReportSimpleResponse;
import co.kr.muldum.presentation.web.dto.response.TeacherMonthReportDetailResponse;
import co.kr.muldum.presentation.web.dto.response.TeacherMonthReportSimpleResponse;
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

    public SubmitMonthReportCommand toSubmitCommand(MonthReportRequest request, UUID userId) { // Changed from Long to UUID
        return new SubmitMonthReportCommand(
                userId,
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

    public TeacherMonthReportDetailResponse toTeacherDetailResponse(TeacherMonthReportApplicationResponse report) {
        return TeacherMonthReportDetailResponse.builder()
                .reportId(report.getReportId())
                .teamId(report.getTeamId())
                .name(report.getName())
                .topic(report.getTopic())
                .goal(report.getGoal())
                .tech(report.getTech())
                .problem(report.getProblem())
                .teacherFeedback(report.getTeacherFeedback())
                .mentorFeedback(report.getMentorFeedback())
                .status(report.getStatus())
                .build();
    }

    public TeacherMonthReportSimpleResponse toTeacherSimpleResponse(TeacherMonthReportApplicationResponse report) {
        return TeacherMonthReportSimpleResponse.builder()
                .reportId(report.getReportId())
                .teamId(report.getTeamId())
                .name(report.getName())
                .topic(report.getTopic())
                .status(report.getStatus())
                .submitedAt(report.getSubmittedAt())
                .build();
    }
}
