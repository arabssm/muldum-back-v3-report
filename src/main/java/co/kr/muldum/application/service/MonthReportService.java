package co.kr.muldum.application.service;

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
import java.util.UUID; // Import UUID

@Service
@Transactional
@RequiredArgsConstructor
public class MonthReportService implements SaveMonthReportUseCase, SubmitMonthReportUseCase, GetStudentMonthReportUseCase, GetTeacherMonthReportUseCase, ScoreMonthReportUseCase {

    private final LoadMonthReportPort loadMonthReportPort;
    private final SaveMonthReportPort saveMonthReportPort;

    @Override
    public MonthReport save(SaveMonthReportCommand command) {
        // Here we would need logic to check if a report for the month already exists
        // For now, we'll just create a new one.
        MonthReport monthReport = MonthReport.builder()
                .userId(command.getUserId())
                .topic(command.getTopic())
                .goal(command.getGoal())
                .tech(command.getTech())
                .problem(command.getProblem())
                .teacherFeedback(command.getTeacherFeedback())
                .mentorFeedback(command.getMentorFeedback())
                .status(ReportStatus.DRAFT)
                .build();
        return saveMonthReportPort.save(monthReport);
    }

    @Override
    public void submit(SubmitMonthReportCommand command) {
        MonthReport monthReport = loadMonthReportPort.findById(command.getReportId())
                .orElseThrow(() -> new RuntimeException("Report not found")); // Replace with specific exception

        // Add authorization logic here to ensure command.getUserId() matches report's user.

        MonthReport updatedReport = MonthReport.builder()
                .id(monthReport.getId())
                .userId(monthReport.getUserId())
                .topic(command.getTopic())
                .goal(command.getGoal())
                .tech(command.getTech())
                .problem(command.getProblem())
                .teacherFeedback(command.getTeacherFeedback())
                .mentorFeedback(command.getMentorFeedback())
                .status(ReportStatus.SUBMIT)
                .submittedAt(LocalDateTime.now())
                .score(monthReport.getScore())
                .build();
        saveMonthReportPort.save(updatedReport);
    }

    @Override
    public MonthReport getByReportId(Long reportId, UUID userId) { // Changed from Long to UUID
        MonthReport report = loadMonthReportPort.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));
        // Add authorization logic here
        if (!report.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }
        return report;
    }

    @Override
    public List<MonthReport> getByUserId(UUID userId) { // Changed from Long to UUID
        return loadMonthReportPort.findByUserId(userId);
    }

    @Override
    public MonthReport getByReportId(Long reportId) {
        return loadMonthReportPort.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));
    }

    @Override
    public List<MonthReport> getByTeamAndMonth(Long teamId, int month) {
        return loadMonthReportPort.findByTeamAndMonth(teamId, month);
    }

    @Override
    public void score(Long reportId, int score) {
        MonthReport monthReport = loadMonthReportPort.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        MonthReport scoredReport = MonthReport.builder()
                .id(monthReport.getId())
                .userId(monthReport.getUserId())
                .topic(monthReport.getTopic())
                .goal(monthReport.getGoal())
                .tech(monthReport.getTech())
                .problem(monthReport.getProblem())
                .teacherFeedback(monthReport.getTeacherFeedback())
                .mentorFeedback(monthReport.getMentorFeedback())
                .status(monthReport.getStatus())
                .submittedAt(monthReport.getSubmittedAt())
                .score(score)
                .build();
        saveMonthReportPort.save(scoredReport);
    }
}
