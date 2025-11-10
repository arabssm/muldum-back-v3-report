package co.kr.muldum.application.usecase;

import java.util.UUID;

public interface ScoreMonthReportUseCase {
    void score(Long reportId, String feedback, UUID teacherId);
}
