package co.kr.muldum.domain.repository;

import co.kr.muldum.domain.model.MonthReport;

import java.util.List;
import java.util.Optional;

public interface MonthReportRepository {

    MonthReport save(MonthReport monthReport);

    Optional<MonthReport> findById(Long reportId);

    List<MonthReport> findByUserId(Long userId);
}
