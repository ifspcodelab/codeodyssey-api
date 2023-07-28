package app.codeodyssey.codeodysseyapi.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ForbiddenException extends RuntimeException {
    private final Resource resource;
    private final ForbiddenType type;
    private final String details;
}
