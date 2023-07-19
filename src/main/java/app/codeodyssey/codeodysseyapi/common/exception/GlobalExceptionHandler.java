package app.codeodyssey.codeodysseyapi.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ProblemDetail> notFound(ResourceNotFoundException ex) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        String resource = ex.getResource().getName();
        String title = "%s not found".formatted(resource);
        String detail = "%s not found with id %s".formatted(resource, ex.getId());

        ProblemDetail problem = ProblemDetail.forStatus(status);
        problem.setTitle(title);
        problem.setDetail(detail);

        log.warn(detail);
        return new ResponseEntity<>(problem, status);
    }

    @ExceptionHandler(ViolationException.class)
    public ResponseEntity<ProblemDetail> violation(ViolationException ex) {
        HttpStatus status = HttpStatus.CONFLICT;
        String resource = ex.getResource().getName();
        String violationType = ex.getType().getName();
        String title = "%s %s".formatted(resource, violationType);
        String details = ex.getDetails();

        ProblemDetail problem = ProblemDetail.forStatus(status);
        problem.setTitle(title);
        problem.setDetail(details);

        log.warn("{} ({})", title, details);
        return new ResponseEntity<>(problem, status);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ProblemDetail> handleTokenExpiredException(TokenExpiredException ex) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        String title = "Token Expired";
        String detail = ex.getMessage();

        ProblemDetail problem = ProblemDetail.forStatus(status);
        problem.setTitle(title);
        problem.setDetail(detail);

        return new ResponseEntity<>(problem, status);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleUserNotFoundException(UserNotFoundException ex) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        String title = "User Not Found";
        String detail = ex.getMessage();

        ProblemDetail problem = ProblemDetail.forStatus(status);
        problem.setTitle(title);
        problem.setDetail(detail);

        log.warn(detail);
        return new ResponseEntity<>(problem, status);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ProblemDetail> handleInvalidTokenException(InvalidTokenException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Invalid Token", ex.getMessage());
    }

    @ExceptionHandler(SendEmailException.class)
    public ResponseEntity<ProblemDetail> handleSendEmailException(SendEmailException ex) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Error Sending Email", ex.getMessage());
    }

    @ExceptionHandler(TokenMalformedException.class)
    public ResponseEntity<ProblemDetail> handleTokenMalformedException(TokenMalformedException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Token Malformed", ex.getMessage());
    }

    @ExceptionHandler(UserIdInvalidException.class)
    public ResponseEntity<ProblemDetail> handleUserIdInvalidException(UserIdInvalidException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Invalid User ID", ex.getMessage());
    }

    private ResponseEntity<ProblemDetail> buildErrorResponse(HttpStatus status, String title, String detail) {
        ProblemDetail problem = ProblemDetail.forStatus(status);
        problem.setTitle(title);
        problem.setDetail(detail);
        return new ResponseEntity<>(problem, status);
    }
}
