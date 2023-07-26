package app.codeodyssey.codeodysseyapi.common.exception;

import lombok.Getter;

import java.util.UUID;

@Getter
public class UnauthorizedAccessException extends RuntimeException {
    private final UUID id;

    public UnauthorizedAccessException(UUID id) {
        super();
        this.id = id;
    }
}
