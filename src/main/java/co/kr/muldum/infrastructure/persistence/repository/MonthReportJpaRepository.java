package co.kr.muldum.infrastructure.persistence.repository;

import co.kr.muldum.infrastructure.persistence.entity.MonthReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.UUID; // Import UUID

public interface MonthReportJpaRepository extends JpaRepository<MonthReportEntity, Long> {
    List<MonthReportEntity> findByUserId(UUID userId); // Changed from Long to UUID
    // We'll need a more complex query for findByTeamAndMonth, maybe a native query or QueryDSL.
    // For now, I'll leave it out of the JpaRepository interface and handle it in the adapter.

    @Query("SELECT m FROM MonthReportEntity m WHERE m.userId = :userId AND FUNCTION('MONTH', m.createdAt) = :month")
    Optional<MonthReportEntity> findByUserIdAndMonth(@Param("userId") UUID userId, @Param("month") int month);
}
