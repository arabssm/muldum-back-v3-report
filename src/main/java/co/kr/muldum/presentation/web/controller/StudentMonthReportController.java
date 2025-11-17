package co.kr.muldum.presentation.web.controller;

import co.kr.muldum.application.usecase.GetStudentMonthReportUseCase;
import co.kr.muldum.application.usecase.SaveMonthReportUseCase;
import co.kr.muldum.application.usecase.SubmitMonthReportUseCase;
import co.kr.muldum.presentation.exception.UnauthorizedRoleException;
import co.kr.muldum.global.exception.UnauthorizedTeamAccessException;
import co.kr.muldum.presentation.web.dto.request.MonthReportRequest;
import co.kr.muldum.presentation.web.dto.response.MessageResponse;
import co.kr.muldum.presentation.web.dto.response.MonthReportDetailResponse;
import co.kr.muldum.presentation.web.dto.response.StudentMonthReportListResponse;
import co.kr.muldum.presentation.web.dto.response.SaveMonthReportResponse;
import co.kr.muldum.presentation.web.mapper.MonthReportWebMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/std/major/report")
public class StudentMonthReportController {

    private static final String STUDENT_ROLE = "ROLE_STUDENT";

    private final SaveMonthReportUseCase saveMonthReportUseCase;
    private final SubmitMonthReportUseCase submitMonthReportUseCase;
    private final GetStudentMonthReportUseCase getStudentMonthReportUseCase;
    private final MonthReportWebMapper monthReportWebMapper;

    @PostMapping("/draft")
    public ResponseEntity<SaveMonthReportResponse> saveDraft(
            @RequestHeader(value = "X-User-Id") Long userId,
            @RequestHeader(value = "X-Team-Id") Long teamId,
            @RequestHeader(value = "X-User-Role") String userRole,
            @RequestBody MonthReportRequest request
    ) {
        validateStudentRole(userRole);
        validateTeamAccess(userId, teamId);
        var savedReport = saveMonthReportUseCase.save(monthReportWebMapper.toCommand(request, userId, teamId));
        var response = new SaveMonthReportResponse(savedReport.getId(), "임시 저장되었습니다.");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/{report_id}")
    public ResponseEntity<SaveMonthReportResponse> updateDraft(
            @RequestHeader(value = "X-User-Id") Long userId,
            @RequestHeader(value = "X-Team-Id") Long teamId,
            @RequestHeader(value = "X-User-Role") String userRole,
            @PathVariable("report_id") Long reportId,
            @RequestBody MonthReportRequest request
    ) {
        validateStudentRole(userRole);
        validateTeamAccess(userId, teamId);
        getStudentMonthReportUseCase.getByReportId(reportId, userId, teamId);
        var savedReport = saveMonthReportUseCase.save(monthReportWebMapper.toCommand(request, userId, teamId));
        var response = new SaveMonthReportResponse(savedReport.getId(), "수정 사항이 임시 저장되었습니다.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{report_id}/submit")
    public ResponseEntity<MessageResponse> submitMonthReport(
            @RequestHeader(value = "X-User-Id") Long userId,
            @RequestHeader(value = "X-Team-Id") Long teamId,
            @RequestHeader(value = "X-User-Role") String userRole,
            @PathVariable("report_id") Long reportId,
            @RequestBody MonthReportRequest request
    ) {
        validateStudentRole(userRole);
        validateTeamAccess(userId, teamId);
        getStudentMonthReportUseCase.getByReportId(reportId, userId, teamId);
        submitMonthReportUseCase.submit(monthReportWebMapper.toSubmitCommand(request, userId, teamId, reportId));
        return new ResponseEntity<>(new MessageResponse("제출되었습니다."), HttpStatus.OK);
    }

    @GetMapping("/{report_id}")
    public ResponseEntity<MonthReportDetailResponse> getStudentMonthReportById(
            @RequestHeader(value = "X-User-Id") Long userId,
            @RequestHeader(value = "X-Team-Id") Long teamId,
            @RequestHeader(value = "X-User-Role") String userRole,
            @PathVariable("report_id") Long reportId
    ) {
        validateStudentRole(userRole);
        validateTeamAccess(userId, teamId);
        var report = getStudentMonthReportUseCase.getByReportId(reportId, userId, teamId);
        return new ResponseEntity<>(monthReportWebMapper.toDetailResponse(report), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<StudentMonthReportListResponse> getStudentMonthReports(
            @RequestHeader(value = "X-User-Id") Long userId,
            @RequestHeader(value = "X-Team-Id") Long teamId,
            @RequestHeader(value = "X-User-Role") String userRole
    ) {
        validateStudentRole(userRole);
        validateTeamAccess(userId, teamId);
        var reports = getStudentMonthReportUseCase.getByUserId(userId, teamId);
        var reportResponses = reports.stream()
                .map(monthReportWebMapper::toSimpleResponse)
                .collect(Collectors.toList());
        var response = StudentMonthReportListResponse.builder()
                .month(LocalDate.now().getMonthValue())
                .reports(reportResponses)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void validateStudentRole(String userRole) {
        if (!STUDENT_ROLE.equals(userRole)) {
            throw new UnauthorizedRoleException(userRole);
        }
    }

    private void validateTeamAccess(Long userId, Long teamId) {
        if (teamId == null || teamId <= 0) {
            throw new UnauthorizedTeamAccessException(userId, teamId);
        }
    }
}
