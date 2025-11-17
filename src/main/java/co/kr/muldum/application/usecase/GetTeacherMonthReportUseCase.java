package co.kr.muldum.application.usecase;

import co.kr.muldum.application.dto.response.TeacherMonthReportApplicationResponse;

import java.util.List;

public interface GetTeacherMonthReportUseCase {
    TeacherMonthReportApplicationResponse getTeacherByReportId(Long reportId, Long teacherId);
    List<TeacherMonthReportApplicationResponse> getByTeamAndMonth(Long teamId, int month, Long teacherId);
}
