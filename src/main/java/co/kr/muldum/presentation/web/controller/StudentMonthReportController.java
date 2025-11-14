package co.kr.muldum.presentation.web.controller;

import co.kr.muldum.application.usecase.GetStudentMonthReportUseCase;
import co.kr.muldum.application.usecase.SaveMonthReportUseCase;
import co.kr.muldum.application.usecase.SubmitMonthReportUseCase;
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
        var savedReport = saveMonthReportUseCase.save(monthReportWebMapper.toCommand(request, userId));
        var response = new SaveMonthReportResponse(savedReport.getId(), "임시 저장되었습니다.");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/{reportId}")
    public ResponseEntity<SaveMonthReportResponse> updateDraft(
            @RequestHeader(value = "X-User-Id") Long userId,
            @RequestHeader(value = "X-Team-Id") Long teamId,
            @RequestHeader(value = "X-User-Role") String userRole,
            @PathVariable Long reportId,
            @RequestBody MonthReportRequest request
    ) {
        var savedReport = saveMonthReportUseCase.save(monthReportWebMapper.toCommand(request, userId));
        var response = new SaveMonthReportResponse(savedReport.getId(), "수정 사항이 임시 저장되었습니다.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/submit")
    public ResponseEntity<MessageResponse> submitMonthReport(
            @RequestHeader(value = "X-User-Id") Long userId,
            @RequestHeader(value = "X-Team-Id") Long teamId,
            @RequestHeader(value = "X-User-Role") String userRole,
            @RequestBody MonthReportRequest request
    ) {
        submitMonthReportUseCase.submit(monthReportWebMapper.toSubmitCommand(request, userId));
        return new ResponseEntity<>(new MessageResponse("제출되었습니다."), HttpStatus.OK);
    }

    @GetMapping("/{reportId}")
    public ResponseEntity<MonthReportDetailResponse> getStudentMonthReportById(
            @RequestHeader(value = "X-User-Id") Long userId,
            @RequestHeader(value = "X-Team-Id") Long teamId,
            @RequestHeader(value = "X-User-Role") String userRole,
            @PathVariable Long reportId
    ) {
        var report = getStudentMonthReportUseCase.getByReportId(reportId, userId);
        return new ResponseEntity<>(monthReportWebMapper.toDetailResponse(report), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<StudentMonthReportListResponse> getStudentMonthReports(
            @RequestHeader(value = "X-User-Id") Long userId,
            @RequestHeader(value = "X-Team-Id") Long teamId,
            @RequestHeader(value = "X-User-Role") String userRole
    ) {
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
}
