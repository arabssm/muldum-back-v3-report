package co.kr.muldum.presentation.web.controller;

import co.kr.muldum.application.usecase.GetStudentMonthReportUseCase;
import co.kr.muldum.application.usecase.GetTeacherMonthReportUseCase;
import co.kr.muldum.application.usecase.SaveMonthReportUseCase;
import co.kr.muldum.application.usecase.ScoreMonthReportUseCase;
import co.kr.muldum.application.usecase.SubmitMonthReportUseCase;
import co.kr.muldum.presentation.web.dto.request.FeedbackRequest;
import co.kr.muldum.presentation.web.dto.request.MonthReportRequest;
import co.kr.muldum.presentation.web.dto.response.MessageResponse;
import co.kr.muldum.presentation.web.dto.response.MonthReportDetailResponse;
import co.kr.muldum.presentation.web.dto.response.StudentMonthReportListResponse;
import co.kr.muldum.presentation.web.dto.response.TeacherMonthReportDetailResponse;
import co.kr.muldum.presentation.web.dto.response.TeacherMonthReportListResponse;
import co.kr.muldum.presentation.web.mapper.MonthReportWebMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;
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

    @PostMapping("/month_report")
    public ResponseEntity<MessageResponse> saveMonthReport(
            @AuthenticationPrincipal UUID userId,
            @RequestBody MonthReportRequest request) {
        var command = monthReportWebMapper.toCommand(request, userId);
        saveMonthReportUseCase.save(command);
        return new ResponseEntity<>(new MessageResponse("수정 사항이 임시 저장되었습니다."), HttpStatus.CREATED);
    }

    @PostMapping("/month_report/submit")
    public ResponseEntity<MessageResponse> submitMonthReport(
            @AuthenticationPrincipal UUID userId,
            @RequestBody MonthReportRequest request) {
        var command = monthReportWebMapper.toSubmitCommand(request, userId);
        submitMonthReportUseCase.submit(command);
        return new ResponseEntity<>(new MessageResponse("제출되었습니다."), HttpStatus.OK);
    }

    @GetMapping("/std/month_report/{reportId}")
    public ResponseEntity<MonthReportDetailResponse> getStudentMonthReportById(
            @AuthenticationPrincipal UUID userId,
            @PathVariable Long reportId) {
        var report = getStudentMonthReportUseCase.getByReportId(reportId, userId);
        return new ResponseEntity<>(monthReportWebMapper.toDetailResponse(report), HttpStatus.OK);
    }

    @GetMapping("/std/month_report")
    public ResponseEntity<StudentMonthReportListResponse> getStudentMonthReports(
            @AuthenticationPrincipal UUID userId) {
        var reports = getStudentMonthReportUseCase.getByUserId(userId);
        var reportResponses = reports.stream()
                .map(monthReportWebMapper::toSimpleResponse)
                .collect(Collectors.toList());
        var response = StudentMonthReportListResponse.builder()
                .month(LocalDate.now().getMonthValue())
                .reports(reportResponses)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/tch/month_report/{reportId}")
    public ResponseEntity<TeacherMonthReportDetailResponse> getTeacherMonthReportById(
            @AuthenticationPrincipal UUID teacherId,
            @PathVariable Long reportId) {
        var report = getTeacherMonthReportUseCase.getTeacherByReportId(reportId, teacherId);
        return new ResponseEntity<>(monthReportWebMapper.toTeacherDetailResponse(report), HttpStatus.OK);
    }

    @GetMapping("/tch/major/report")
    public ResponseEntity<TeacherMonthReportListResponse> getTeacherMonthReportsByTeamAndMonth(
            @AuthenticationPrincipal UUID teacherId,
            @RequestParam(required = false) Long team,
            @RequestParam(required = false) Integer month) {
        var reports = getTeacherMonthReportUseCase.getByTeamAndMonth(team, month, teacherId);
        var reportResponses = reports.stream()
                .map(monthReportWebMapper::toTeacherSimpleResponse)
                .collect(Collectors.toList());
        var response = TeacherMonthReportListResponse.builder()
                .month(month != null ? month : LocalDate.now().getMonthValue())
                .reports(reportResponses)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/tch/major/report/{reportId}")
    public ResponseEntity<MessageResponse> scoreMonthReport(
            @AuthenticationPrincipal UUID teacherId,
            @PathVariable Long reportId,
            @RequestBody FeedbackRequest request) {
        scoreMonthReportUseCase.score(reportId, request.getFeedback(), teacherId);
        return new ResponseEntity<>(new MessageResponse("채점 완료"), HttpStatus.OK);
    }
}
