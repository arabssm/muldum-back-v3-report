package co.kr.muldum.application.usecase;

import co.kr.muldum.domain.model.MonthReport;

import java.util.List;

public interface GetStudentMonthReportUseCase {
    MonthReport getByReportId(Long reportId, Long userId);
    List<MonthReport> getByUserId(Long userId);
}
