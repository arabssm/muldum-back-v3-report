package co.kr.muldum.application.usecase;

import co.kr.muldum.application.dto.response.TeacherMonthReportApplicationResponse;
import java.util.List;
import java.util.UUID;

public interface GetTeacherMonthReportUseCase {
    TeacherMonthReportApplicationResponse getTeacherByReportId(Long reportId, UUID teacherId);
    List<TeacherMonthReportApplicationResponse> getByTeamAndMonth(Long teamId, int month, UUID teacherId);
}
