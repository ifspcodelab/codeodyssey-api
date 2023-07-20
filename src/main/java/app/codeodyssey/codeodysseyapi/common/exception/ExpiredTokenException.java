package app.codeodyssey.codeodysseyapi.common.exception;

import lombok.Getter;

@Getter
public class ExpiredTokenException extends RuntimeException {
    private final String message;
    private final int httpStatus;

    public ExpiredTokenException(String message, int httpStatus) {
        super(message);
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
