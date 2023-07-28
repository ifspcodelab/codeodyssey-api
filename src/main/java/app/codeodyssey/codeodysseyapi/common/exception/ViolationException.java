package app.codeodyssey.codeodysseyapi.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@AllArgsConstructor
@Getter
@ResponseStatus(HttpStatus.CONFLICT)
public class ViolationException extends RuntimeException {
    private final Resource resource;
    private final ViolationType type;
    private final String details;
}
