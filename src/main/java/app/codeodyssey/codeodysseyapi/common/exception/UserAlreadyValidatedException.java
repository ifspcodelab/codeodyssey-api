package app.codeodyssey.codeodysseyapi.common.exception;


import lombok.Getter;

@Getter
public class UserAlreadyValidatedException extends RuntimeException {
    private final String message;
    private final int httpStatus;

    public UserAlreadyValidatedException(String message, int httpStatus) {
        super(message);
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
