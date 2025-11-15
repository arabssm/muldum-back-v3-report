package co.kr.muldum.presentation.web.controller;

import co.kr.muldum.application.usecase.GetTeacherMonthReportUseCase;
import co.kr.muldum.application.usecase.ScoreMonthReportUseCase;
import co.kr.muldum.presentation.exception.UnauthorizedRoleException;
import co.kr.muldum.presentation.web.dto.request.FeedbackRequest;
import co.kr.muldum.presentation.web.dto.response.MessageResponse;
import co.kr.muldum.presentation.web.dto.response.TeacherMonthReportDetailResponse;
import co.kr.muldum.presentation.web.dto.response.TeacherMonthReportListResponse;
import co.kr.muldum.presentation.web.mapper.MonthReportWebMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tch/major/report")
public class TeacherMonthReportController {

    private final GetTeacherMonthReportUseCase getTeacherMonthReportUseCase;
    private final ScoreMonthReportUseCase scoreMonthReportUseCase;
    private final MonthReportWebMapper monthReportWebMapper;

    @GetMapping("/{reportId}")
    public ResponseEntity<TeacherMonthReportDetailResponse> getTeacherMonthReportById(
            @RequestHeader(value = "X-User-Id") Long teacherId,
            @RequestHeader(value = "X-User-Role") String userRole,
            @PathVariable Long reportId
    ) {
        validateTeacherRole(userRole);
        var report = getTeacherMonthReportUseCase.getTeacherByReportId(reportId, teacherId);
        return new ResponseEntity<>(monthReportWebMapper.toTeacherDetailResponse(report), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<TeacherMonthReportListResponse> getTeacherMonthReportsByTeamAndMonth(
            @RequestHeader(value = "X-User-Id") Long teacherId,
            @RequestHeader(value = "X-User-Role") String userRole,
            @RequestParam Long team,
            @RequestParam Integer month
    ) {
        validateTeacherRole(userRole);
        Objects.requireNonNull(team, "team parameter is required");
        Objects.requireNonNull(month, "month parameter is required");
        var reports = getTeacherMonthReportUseCase.getByTeamAndMonth(team, month, teacherId);
        var reportResponses = reports.stream()
                .map(monthReportWebMapper::toTeacherSimpleResponse)
                .collect(Collectors.toList());
        var response = TeacherMonthReportListResponse.builder()
                .month(month)
                .reports(reportResponses)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{reportId}")
    public ResponseEntity<MessageResponse> scoreMonthReport(
            @RequestHeader(value = "X-User-Id") Long teacherId,
            @RequestHeader(value = "X-User-Role") String userRole,
            @PathVariable Long reportId,
            @RequestBody FeedbackRequest request
    ) {
        validateTeacherRole(userRole);
        scoreMonthReportUseCase.score(reportId, request.getFeedback(), teacherId);
        return new ResponseEntity<>(new MessageResponse("채점 완료"), HttpStatus.OK);
    }

    private void validateTeacherRole(String userRole) {
        if (!"ROLE_TEACHER".equals(userRole)) {
            throw new UnauthorizedRoleException(userRole);
        }
    }
}
