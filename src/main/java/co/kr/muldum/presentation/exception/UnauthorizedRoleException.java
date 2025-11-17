package co.kr.muldum.presentation.exception;

public class UnauthorizedRoleException extends RuntimeException {
    public UnauthorizedRoleException(String role) {
        super("권한이 없습니다: " + role);
    }
}
