package app.codeodyssey.codeodysseyapi.common.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final MessageSource messageSource;

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

    @ExceptionHandler(NoneExistentTokenException.class)
    public ResponseEntity<ProblemDetail> NoneExistentTokenException(NoneExistentTokenException ex) {
        log.warn("Token problem: {}", ex.getMessage());

        HttpStatus status = HttpStatus.NOT_FOUND;
        String title = "Token problem";
        String detail = ex.getMessage();

        ProblemDetail problem = ProblemDetail.forStatus(status);
        problem.setTitle(title);
        problem.setDetail(detail);

        return new ResponseEntity<>(problem, status);
    }

    @ExceptionHandler(ExpiredTokenException.class)
    public ResponseEntity<ProblemDetail> ExpiredTokenException(ExpiredTokenException ex) {
        log.warn("Token problem: {}", ex.getMessage());

        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
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
        List<String> details = new ArrayList<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String errorMessage = messageSource.getMessage(error, LocaleContextHolder.getLocale());
            details.add(errorMessage);

        });
        ProblemDetail problem = ProblemDetail.forStatus(status);
        problem.setTitle(title);
        problem.setDetail(details.get(0));

        log.warn("{} - {}", title, details.get(0));

        details.clear();

        return new ResponseEntity<>(problem, status);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ProblemDetail> badCredentials(BadCredentialsException ex) {
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
    public ResponseEntity<ProblemDetail> forbidden(ForbiddenException ex) {
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

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ProblemDetail> userNotFound(UserNotFoundException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String resource = ex.getResource().getName();
        String message = ex.getMessage();

        ProblemDetail problem = ProblemDetail.forStatus(status);
        problem.setTitle(resource);
        problem.setDetail(message);

        log.warn("{} ({})", problem, status);
        return new ResponseEntity<>(problem, status);
    }

    @ExceptionHandler(ForbiddenAccessException.class)
    public ResponseEntity<ProblemDetail> forbiddendAccess(ForbiddenAccessException ex) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        UUID userId = ex.getId();
        String title = "Forbidden access";
        String detail = "User with id %s does not have authority to access this content.".formatted(userId);

        ProblemDetail problem = ProblemDetail.forStatus(status);
        problem.setTitle(title);
        problem.setDetail(detail);

        log.warn(detail);
        return new ResponseEntity<>(problem, status);
    }

    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<ProblemDetail> emailNotFound(EmailNotFoundException ex) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        String email = ex.getEmail();
        String title = "Email not found";
        String detail = "Email not found with address %s".formatted(email);

        ProblemDetail problem = ProblemDetail.forStatus(status);
        problem.setTitle(title);
        problem.setDetail(detail);

        log.warn(detail);
        return new ResponseEntity<>(problem, status);
    }

    @ExceptionHandler(StudentAlreadyEnrolledException.class)
    public ResponseEntity<ProblemDetail> studentAlreadyEnrolled(StudentAlreadyEnrolledException ex) {
        HttpStatus status = HttpStatus.CONFLICT;
        UUID studentId = ex.getStudentId();
        UUID courseId = ex.getCourseId();
        String title = "Student already enrolled";
        String detail = "Student with id=%s is already enrolled on course id=%s.".formatted(studentId, courseId);

        ProblemDetail problem = ProblemDetail.forStatus(status);
        problem.setTitle(title);
        problem.setDetail(detail);

        log.warn(detail);
        return new ResponseEntity<>(problem, status);
    }

    @ExceptionHandler(ResendEmailException.class)
    public ResponseEntity<ProblemDetail> resendEmailDelay(ResendEmailException ex) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        String email = ex.getEmail();
        String title = "Time to resend not passed";
        String detail = "One minute to resend email to %s has not been passed yet".formatted(email);

        ProblemDetail problem = ProblemDetail.forStatus(status);
        problem.setTitle(title);
        problem.setDetail(detail);

        log.warn(detail);
        return new ResponseEntity<>(problem, status);
    }

    @ExceptionHandler(InvitationLinkExpiredException.class)
    public ResponseEntity<ProblemDetail> invitationLinkExpired(InvitationLinkExpiredException ex) {
        HttpStatus status = HttpStatus.GONE;
        UUID invitationId = ex.getInvitationId();
        UUID courseId = ex.getCourseId();
        String title = "Invitation link expired";
        String detail = "Invitation with id=%s is expired on course id=%s.".formatted(invitationId, courseId);

        ProblemDetail problem = ProblemDetail.forStatus(status);
        problem.setTitle(title);
        problem.setDetail(detail);

        log.warn(detail);
        return new ResponseEntity<>(problem, status);
    }

    @ExceptionHandler(StudentNotEnrolledException.class)
    public ResponseEntity<ProblemDetail> studentNotEnrolled(StudentNotEnrolledException ex) {
        HttpStatus status = HttpStatus.CONFLICT;
        UUID studentId = ex.getStudentId();
        UUID courseId = ex.getCourseId();
        String title = "Student not enrolled";
        String detail = "Student with id=%s is not enrolled on course with id=%s.".formatted(studentId, courseId);

        ProblemDetail problem = ProblemDetail.forStatus(status);
        problem.setTitle(title);
        problem.setDetail(detail);

        log.warn(detail);
        return new ResponseEntity<>(problem, status);
    }
}
