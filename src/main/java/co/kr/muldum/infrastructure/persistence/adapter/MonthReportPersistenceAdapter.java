package co.kr.muldum.infrastructure.persistence.adapter;

import co.kr.muldum.application.port.out.LoadMonthReportPort;
import co.kr.muldum.application.port.out.SaveMonthReportPort;
import co.kr.muldum.domain.model.MonthReport;
import co.kr.muldum.infrastructure.persistence.repository.MonthReportJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID; // Import UUID
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MonthReportPersistenceAdapter implements LoadMonthReportPort, SaveMonthReportPort {

    private final MonthReportJpaRepository monthReportJpaRepository;
    private final MonthReportMapper monthReportMapper;

    @Override
    public Optional<MonthReport> findById(Long reportId) {
        return monthReportJpaRepository.findById(reportId)
                .map(monthReportMapper::toDomain);
    }

    @Override
    public List<MonthReport> findByUserId(UUID userId) { // Changed from Long to UUID
        return monthReportJpaRepository.findByUserId(userId).stream()
                .map(monthReportMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<MonthReport> findByTeamAndMonth(Long teamId, int month) {
        // This requires a custom query that joins with a Team/User table.
        // As those domain models are not defined, I will return an empty list for now.
        return Collections.emptyList();
    }

    @Override
    public Optional<MonthReport> findByUserIdAndMonth(UUID userId, int month) {
        return monthReportJpaRepository.findByUserIdAndMonth(userId, month)
                .map(monthReportMapper::toDomain);
    }

    @Override
    public MonthReport save(MonthReport monthReport) {
        var entity = monthReportMapper.toEntity(monthReport);
        var savedEntity = monthReportJpaRepository.save(entity);
        return monthReportMapper.toDomain(savedEntity);
    }
}
