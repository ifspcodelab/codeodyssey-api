package app.codeodyssey.codeodysseyapi.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ViolationException extends RuntimeException {
    private final Resource resource;
    private final ViolationType type;
    private final String details;
}