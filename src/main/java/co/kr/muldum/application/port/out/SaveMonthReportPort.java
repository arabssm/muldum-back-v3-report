package co.kr.muldum.application.port.out;

import co.kr.muldum.domain.model.MonthReport;

public interface SaveMonthReportPort {
    MonthReport save(MonthReport monthReport);
}
