package app.codeodyssey.codeodysseyapi.common.exception;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String message) {
        super(message);
    }
    public InvalidTokenException(String message, NumberFormatException ex) {
        super(message, ex);
    }

}
