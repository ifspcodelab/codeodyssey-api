package app.codeodyssey.codeodysseyapi.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.UUID;

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

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ProblemDetail> badCredentials(BadCredentialsException ex){
        HttpStatus status = HttpStatus.FORBIDDEN;
        String className = ex.getClass().getName();
        String message = ex.getMessage();

        ProblemDetail problem = ProblemDetail.forStatus(status);
        problem.setTitle(className);
        problem.setDetail(message);

        log.warn("{} ({})", className, message);
        return new ResponseEntity<>(problem, status);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ProblemDetail> forbidden(ForbiddenException ex){
        HttpStatus status = HttpStatus.FORBIDDEN;
        String resource = ex.getResource().getName();
        String forbiddenType = ex.getType().getName();
        String title = "%s %s".formatted(resource, forbiddenType);
        String details = ex.getDetails();

        ProblemDetail problem = ProblemDetail.forStatus(status);
        problem.setTitle(title);
        problem.setDetail(details);

        log.warn("{} ({})", title, details);
        return new ResponseEntity<>(problem, status);
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ProblemDetail> businessRule(BusinessRuleException ex) {
        HttpStatus status = HttpStatus.CONFLICT;
        String resource = ex.getResource().getName();
        String businessRuleType = ex.getBusinessRuleType().getName();
        String title = "%s %s".formatted(resource, businessRuleType);
        String details = ex.getDetails();

        ProblemDetail problem = ProblemDetail.forStatus(status);
        problem.setTitle(title);
        problem.setDetail(details);

        log.warn("{} ({})", title, details);
        return new ResponseEntity<>(problem, status);
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<ProblemDetail> unauthorizedAccess(UnauthorizedAccessException ex) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        UUID userId = ex.getId();
        String title = "Unauthorized access";
        String detail = "User with id %s not authorized to access this content.".formatted(userId);

        ProblemDetail problem = ProblemDetail.forStatus(status);
        problem.setTitle(title);
        problem.setDetail(detail);

        log.warn(detail);
        return new ResponseEntity<>(problem, status);
    }
}
