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
}
