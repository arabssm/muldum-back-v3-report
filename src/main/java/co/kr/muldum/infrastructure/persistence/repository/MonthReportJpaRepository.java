package co.kr.muldum.infrastructure.persistence.repository;

import co.kr.muldum.infrastructure.persistence.entity.MonthReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID; // Import UUID

public interface MonthReportJpaRepository extends JpaRepository<MonthReportEntity, Long> {
    List<MonthReportEntity> findByUserId(UUID userId); // Changed from Long to UUID
    // We'll need a more complex query for findByTeamAndMonth, maybe a native query or QueryDSL.
    // For now, I'll leave it out of the JpaRepository interface and handle it in the adapter.
}
