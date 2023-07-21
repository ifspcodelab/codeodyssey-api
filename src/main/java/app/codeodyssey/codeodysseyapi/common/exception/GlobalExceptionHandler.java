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


    @ExceptionHandler(UserAlreadyValidatedException.class)
    public ResponseEntity<ProblemDetail> alreadyValidated(UserAlreadyValidatedException ex) {
        log.warn("Validation: {}", ex.getMessage());

        HttpStatus status = HttpStatus.CONFLICT;
        String title = "Validation";
        String detail = ex.getMessage();

        ProblemDetail problem = ProblemDetail.forStatus(status);
        problem.setTitle(title);
        problem.setDetail(detail);

        return new ResponseEntity<>(problem, status);
    }

    @ExceptionHandler(TokenException.class)
    public ResponseEntity<ProblemDetail> tokenException(TokenException ex) {
        log.warn("Token problem: {}", ex.getMessage());

        HttpStatus status = HttpStatus.UNAUTHORIZED;
        String title = "Token problem";
        String detail = ex.getMessage();

        ProblemDetail problem = ProblemDetail.forStatus(status);
        problem.setTitle(title);
        problem.setDetail(detail);

        return new ResponseEntity<>(problem, status);
    }
}
