package co.kr.muldum.presentation.web.controller;

import co.kr.muldum.application.usecase.*;
import co.kr.muldum.presentation.web.dto.request.MonthReportRequest;
import co.kr.muldum.presentation.web.dto.request.ScoreRequest;
import co.kr.muldum.presentation.web.dto.response.MonthReportDetailResponse;
import co.kr.muldum.presentation.web.dto.response.MonthReportSimpleResponse;
import co.kr.muldum.presentation.web.dto.response.TeacherMonthReportDetailResponse;
import co.kr.muldum.presentation.web.dto.response.TeacherMonthReportSimpleResponse;
import co.kr.muldum.presentation.web.mapper.MonthReportWebMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID; // Import UUID
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MonthReportController {

    private final SaveMonthReportUseCase saveMonthReportUseCase;
    private final SubmitMonthReportUseCase submitMonthReportUseCase;
    private final GetStudentMonthReportUseCase getStudentMonthReportUseCase;
    private final GetTeacherMonthReportUseCase getTeacherMonthReportUseCase;
    private final ScoreMonthReportUseCase scoreMonthReportUseCase;
    private final MonthReportWebMapper monthReportWebMapper;

    // POST /month_report - Create/Update draft
    @PostMapping("/month_report")
    public ResponseEntity<MonthReportDetailResponse> saveMonthReport(
            @AuthenticationPrincipal UUID userId, // Use UUID directly
            @RequestBody MonthReportRequest request) {
        var command = monthReportWebMapper.toCommand(request, userId);
        var savedReport = saveMonthReportUseCase.save(command);
        return new ResponseEntity<>(monthReportWebMapper.toDetailResponse(savedReport), HttpStatus.CREATED);
    }

    // POST /month_report/submit - Submit report
    @PostMapping("/month_report/submit")
    public ResponseEntity<String> submitMonthReport(
            @AuthenticationPrincipal UUID userId, // Use UUID directly
            @RequestBody MonthReportRequest request, // Reusing MonthReportRequest for submission
            @RequestParam Long reportId) { // Assuming reportId is passed as a query param for submission
        var command = monthReportWebMapper.toCommand(request, userId, reportId);
        submitMonthReportUseCase.submit(command);
        return new ResponseEntity<>("제출되었습니다.", HttpStatus.OK);
    }

    // GET /std/month_report/{report_id} - Get student's specific report
    @GetMapping("/std/month_report/{reportId}")
    public ResponseEntity<MonthReportDetailResponse> getStudentMonthReportById(
            @AuthenticationPrincipal UUID userId, // Use UUID directly
            @PathVariable Long reportId) {
        var report = getStudentMonthReportUseCase.getByReportId(reportId, userId);
        return new ResponseEntity<>(monthReportWebMapper.toDetailResponse(report), HttpStatus.OK);
    }

    // GET /std/month_report - Get student's all reports
    @GetMapping("/std/month_report")
    public ResponseEntity<List<MonthReportSimpleResponse>> getStudentMonthReports(
            @AuthenticationPrincipal UUID userId) { // Use UUID directly
        var reports = getStudentMonthReportUseCase.getByUserId(userId);
        var responses = reports.stream()
                .map(monthReportWebMapper::toSimpleResponse)
                .collect(Collectors.toList());
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    // GET /tch/month_report/{report_id} - Get teacher's specific report
    @GetMapping("/tch/month_report/{reportId}")
    public ResponseEntity<TeacherMonthReportDetailResponse> getTeacherMonthReportById(
            @AuthenticationPrincipal UUID teacherId, // Use UUID directly for teacher
            @PathVariable Long reportId) {
        // Teacher authorization check would be here, e.g., check roles of authenticatedUser
        var report = getTeacherMonthReportUseCase.getByReportId(reportId);
        // Assuming a way to get teamId and name from report.getUserId()
        // For now, dummy values
        return new ResponseEntity<>(TeacherMonthReportDetailResponse.builder()
                .reportId(report.getId())
                .teamId(1L) // Dummy teamId
                .name("Dummy User") // Dummy user name
                .topic(report.getTopic())
                .goal(report.getGoal())
                .tech(report.getTech())
                .problem(report.getProblem())
                .teacherFeedback(report.getTeacherFeedback())
                .mentorFeedback(report.getMentorFeedback())
                .status(report.getStatus())
                .build(), HttpStatus.OK);
    }

    // GET /tch/major/report?team={team_id}&month={month} - Get teacher's reports by team and month
    @GetMapping("/tch/major/report")
    public ResponseEntity<List<TeacherMonthReportSimpleResponse>> getTeacherMonthReportsByTeamAndMonth(
            @AuthenticationPrincipal UUID teacherId, // Use UUID directly for teacher
            @RequestParam(required = false) Long team,
            @RequestParam(required = false) Integer month) {
        // Teacher authorization check would be here
        // For now, team and month are not fully implemented in persistence, so returning empty list
        List<TeacherMonthReportSimpleResponse> responses = List.of(); // Placeholder
        // In a real scenario:
        // var reports = getTeacherMonthReportUseCase.getByTeamAndMonth(team, month);
        // var responses = reports.stream()
        //         .map(report -> TeacherMonthReportSimpleResponse.builder()
        //                 .reportId(report.getId())
        //                 .teamId(team) // Need to get actual teamId
        //                 .name("User Name") // Need to get actual user name
        //                 .topic(report.getTopic())
        //                 .status(report.getStatus())
        //                 .submitedAt(report.getSubmittedAt())
        //                 .build())
        //         .collect(Collectors.toList());
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    // POST /tch/major/report/{report_id} - Score a report
    @PostMapping("/tch/major/report/{reportId}")
    public ResponseEntity<String> scoreMonthReport(
            @AuthenticationPrincipal UUID teacherId, // Use UUID directly for teacher
            @PathVariable Long reportId,
            @RequestBody ScoreRequest request) {
        // Teacher authorization check would be here
        scoreMonthReportUseCase.score(reportId, request.getScore());
        return new ResponseEntity<>("채점 완료", HttpStatus.OK);
    }
}
