package app.codeodyssey.codeodysseyapi.common.exception;

public class TokenMalformedException extends RuntimeException {
    public TokenMalformedException(String message) {
        super(message);
    }
}