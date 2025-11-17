package co.kr.muldum.application.service;

import co.kr.muldum.application.dto.response.TeacherMonthReportApplicationResponse;
import co.kr.muldum.application.port.in.SaveMonthReportCommand;
import co.kr.muldum.application.port.in.SubmitMonthReportCommand;
import co.kr.muldum.application.port.out.LoadMonthReportPort;
import co.kr.muldum.application.port.out.SaveMonthReportPort;
import co.kr.muldum.application.usecase.*;
import co.kr.muldum.domain.model.MonthReport;
import co.kr.muldum.domain.model.ReportStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MonthReportService implements SaveMonthReportUseCase, SubmitMonthReportUseCase, GetStudentMonthReportUseCase, GetTeacherMonthReportUseCase, ScoreMonthReportUseCase {

    private final LoadMonthReportPort loadMonthReportPort;
    private final SaveMonthReportPort saveMonthReportPort;

    @Override
    public MonthReport save(SaveMonthReportCommand command) {
        int currentMonth = LocalDateTime.now().getMonthValue();
        Optional<MonthReport> existingReport = loadMonthReportPort.findByUserIdAndMonth(command.getUserId(), currentMonth);

        MonthReport monthReport = existingReport.map(report -> MonthReport.builder()
                .id(report.getId())
                .userId(report.getUserId())
                .topic(command.getTopic())
                .goal(command.getGoal())
                .tech(command.getTech())
                .problem(command.getProblem())
                .teacherFeedback(command.getTeacherFeedback())
                .mentorFeedback(command.getMentorFeedback())
                .status(report.getStatus())
                .submittedAt(report.getSubmittedAt())
                .score(report.getScore())
                .createdAt(report.getCreatedAt())
                .build()
        ).orElseGet(() -> MonthReport.builder()
                .userId(command.getUserId())
                .topic(command.getTopic())
                .goal(command.getGoal())
                .tech(command.getTech())
                .problem(command.getProblem())
                .teacherFeedback(command.getTeacherFeedback())
                .mentorFeedback(command.getMentorFeedback())
                .status(ReportStatus.DRAFT)
                .build());

        return saveMonthReportPort.save(monthReport);
    }

    @Override
    public void submit(SubmitMonthReportCommand command) {
        int currentMonth = LocalDateTime.now().getMonthValue();
        Optional<MonthReport> existingReport = loadMonthReportPort.findByUserIdAndMonth(command.getUserId(), currentMonth);

        MonthReport monthReport = existingReport.map(report -> MonthReport.builder()
                .id(report.getId())
                .userId(report.getUserId())
                .topic(command.getTopic())
                .goal(command.getGoal())
                .tech(command.getTech())
                .problem(command.getProblem())
                .teacherFeedback(command.getTeacherFeedback())
                .mentorFeedback(command.getMentorFeedback())
                .status(ReportStatus.SUBMIT)
                .submittedAt(LocalDateTime.now())
                .score(report.getScore())
                .createdAt(report.getCreatedAt())
                .build()
        ).orElseGet(() -> MonthReport.builder()
                .userId(command.getUserId())
                .topic(command.getTopic())
                .goal(command.getGoal())
                .tech(command.getTech())
                .problem(command.getProblem())
                .teacherFeedback(command.getTeacherFeedback())
                .mentorFeedback(command.getMentorFeedback())
                .status(ReportStatus.SUBMIT)
                .submittedAt(LocalDateTime.now())
                .build());

        saveMonthReportPort.save(monthReport);
    }

    @Override
    public MonthReport getByReportId(Long reportId, Long userId) {
        MonthReport report = loadMonthReportPort.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));
        // Add authorization logic here
        if (!report.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }
        return report;
    }

    @Override
    public List<MonthReport> getByUserId(Long userId) {
        return loadMonthReportPort.findByUserId(userId);
    }

    @Override
    public TeacherMonthReportApplicationResponse getTeacherByReportId(Long reportId, Long teacherId) {
        // TODO: Add authorization logic to check if the teacher is authorized to view this report.
        MonthReport report = loadMonthReportPort.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));
        // In a real scenario, fetch teamId and name based on report.getUserId()
        return TeacherMonthReportApplicationResponse.builder()
                .reportId(report.getId())
                .userId(report.getUserId())
                .teamId(1L) // Dummy teamId
                .name("Dummy User") // Dummy user name
                .topic(report.getTopic())
                .goal(report.getGoal())
                .tech(report.getTech())
                .problem(report.getProblem())
                .teacherFeedback(report.getTeacherFeedback())
                .mentorFeedback(report.getMentorFeedback())
                .status(report.getStatus())
                .submittedAt(report.getSubmittedAt())
                .score(report.getScore())
                .build();
    }

    @Override
    public List<TeacherMonthReportApplicationResponse> getByTeamAndMonth(Long teamId, int month, Long teacherId) {
        // TODO: Add authorization logic here.
        List<MonthReport> reports = loadMonthReportPort.findByTeamAndMonth(teamId, month);
        return reports.stream()
                .map(report -> TeacherMonthReportApplicationResponse.builder()
                        .reportId(report.getId())
                        .userId(report.getUserId())
                        .teamId(teamId) // Use the provided teamId
                        .name("Dummy User") // Dummy user name
                        .topic(report.getTopic())
                        .status(report.getStatus())
                        .submittedAt(report.getSubmittedAt())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public void score(Long reportId, String feedback, Long teacherId) {
        // TODO: Add authorization logic here.
        MonthReport monthReport = loadMonthReportPort.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        MonthReport scoredReport = MonthReport.builder()
                .id(monthReport.getId())
                .userId(monthReport.getUserId())
                .topic(monthReport.getTopic())
                .goal(monthReport.getGoal())
                .tech(monthReport.getTech())
                .problem(monthReport.getProblem())
                .teacherFeedback(feedback) // Update teacherFeedback with the provided feedback
                .mentorFeedback(monthReport.getMentorFeedback())
                .status(ReportStatus.DRAFT)
                .submittedAt(monthReport.getSubmittedAt())
                .score(monthReport.getScore()) // Keep the existing score, or remove if not applicable
                .createdAt(monthReport.getCreatedAt())
                .build();
        saveMonthReportPort.save(scoredReport);
    }
}
