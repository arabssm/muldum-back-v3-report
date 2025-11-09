package co.kr.muldum.application.usecase;

import co.kr.muldum.application.port.in.SaveMonthReportCommand;
import co.kr.muldum.domain.model.MonthReport;

public interface SaveMonthReportUseCase {
    MonthReport save(SaveMonthReportCommand command);
}
