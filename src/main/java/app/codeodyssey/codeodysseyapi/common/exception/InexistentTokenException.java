package app.codeodyssey.codeodysseyapi.common.exception;

import lombok.Getter;

@Getter
public class InexistentTokenException extends RuntimeException {
    private final String message;
    private final int httpStatus;
    public InexistentTokenException(String messae, int httpStatus) {
        super(messae);
        this.message = messae;
        this.httpStatus = httpStatus;
    }
}
