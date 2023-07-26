package app.codeodyssey.codeodysseyapi.common.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

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

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ProblemDetail> passwordException(InvalidPasswordException ex) {
        log.warn("Password problem: {}", ex.getMessage());

        HttpStatus status = HttpStatus.BAD_REQUEST;
        String title = "Password problem";
        String detail = ex.getMessage();

        ProblemDetail problem = ProblemDetail.forStatus(status);
        problem.setTitle(title);
        problem.setDetail(detail);

        return new ResponseEntity<>(problem, status);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidationException(MethodArgumentNotValidException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String title = "Validation Error";
        StringBuilder detailBuilder = new StringBuilder();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String errorMessage = messageSource.getMessage(error, LocaleContextHolder.getLocale());

            detailBuilder.append(errorMessage);
        });
        String detail = detailBuilder.toString();

        ProblemDetail problem = ProblemDetail.forStatus(status);
        problem.setTitle(title);
        problem.setDetail(detail);

        log.warn("{} - {}", title, detail);

        return new ResponseEntity<>(problem, status);
    }
}
