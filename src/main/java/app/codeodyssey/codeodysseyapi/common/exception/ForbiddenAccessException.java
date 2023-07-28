package app.codeodyssey.codeodysseyapi.common.exception;

import java.util.UUID;
import lombok.Getter;

@Getter
public class ForbiddenAccessException extends RuntimeException {
    private final UUID id;

    public ForbiddenAccessException(UUID id) {
        super();
        this.id = id;
    }
}
