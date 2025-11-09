package co.kr.muldum.application.usecase;

import co.kr.muldum.domain.model.MonthReport;
import java.util.UUID;
import java.util.List;

public interface GetStudentMonthReportUseCase {
    MonthReport getByReportId(Long reportId, UUID userId);
    List<MonthReport> getByUserId(UUID userId);
}
