package app.codeodyssey.codeodysseyapi.common.exception;

import java.util.UUID;
import lombok.Getter;

@Getter
public class UnauthorizedAccessException extends RuntimeException {
    private final UUID id;

    public UnauthorizedAccessException(UUID id) {
        super();
        this.id = id;
    }
}
