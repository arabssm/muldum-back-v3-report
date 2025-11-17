package co.kr.muldum.global.exception;

import co.kr.muldum.presentation.exception.UnauthorizedRoleException;
import co.kr.muldum.presentation.web.dto.response.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UnauthorizedRoleException.class)
    public ResponseEntity<MessageResponse> handleUnauthorizedRole(UnauthorizedRoleException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new MessageResponse(ex.getMessage()));
    }

    @ExceptionHandler(UnauthorizedTeamAccessException.class)
    public ResponseEntity<MessageResponse> handleUnauthorizedTeamAccess(UnauthorizedTeamAccessException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new MessageResponse(ex.getMessage()));
    }

    @ExceptionHandler(UnauthorizedReportAccessException.class)
    public ResponseEntity<MessageResponse> handleUnauthorizedReportAccess(UnauthorizedReportAccessException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new MessageResponse(ex.getMessage()));
    }

    @ExceptionHandler(MonthReportNotFoundException.class)
    public ResponseEntity<MessageResponse> handleMonthReportNotFound(MonthReportNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new MessageResponse(ex.getMessage()));
    }
}
