package co.kr.muldum.application.port.out;

import co.kr.muldum.domain.model.MonthReport;

import java.util.List;
import java.util.Optional;
import java.util.UUID; // Import UUID

public interface LoadMonthReportPort {
    Optional<MonthReport> findById(Long reportId);
    List<MonthReport> findByUserId(UUID userId); // Changed from Long to UUID
    List<MonthReport> findByTeamAndMonth(Long teamId, int month); // This might need adjustment depending on domain model
    Optional<MonthReport> findByUserIdAndMonth(UUID userId, int month);
}
