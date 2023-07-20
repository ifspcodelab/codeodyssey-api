package app.codeodyssey.codeodysseyapi.common.exception;


import lombok.Getter;

@Getter
public class InvalidTokenFormatException extends RuntimeException {
    private final String message;
    private final int httpStatus;

    public InvalidTokenFormatException(String message, int httpStatus) {
        super(message);
        this.message = message;
        this.httpStatus = httpStatus;
    }


}
