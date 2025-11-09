package co.kr.muldum.application.usecase;

import co.kr.muldum.domain.model.MonthReport;
import java.util.List;

public interface GetTeacherMonthReportUseCase {
    MonthReport getByReportId(Long reportId);
    List<MonthReport> getByTeamAndMonth(Long teamId, int month);
}
