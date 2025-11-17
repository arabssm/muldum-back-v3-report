package co.kr.muldum.application.usecase;

public interface ScoreMonthReportUseCase {
    void score(Long reportId, String feedback, Long teacherId);
}
