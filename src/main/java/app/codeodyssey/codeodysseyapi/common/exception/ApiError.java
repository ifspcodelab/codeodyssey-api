package app.codeodyssey.codeodysseyapi.common.exception;

import lombok.Getter;

@Getter
public class ApiError {
    private final String message;

    public ApiError(String message) {
        this.message = message;
    }
}
