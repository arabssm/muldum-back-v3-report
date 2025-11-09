package co.kr.muldum.application.usecase;

import co.kr.muldum.application.port.in.SubmitMonthReportCommand;

public interface SubmitMonthReportUseCase {
    void submit(SubmitMonthReportCommand command);
}
