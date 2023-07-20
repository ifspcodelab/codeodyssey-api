package app.codeodyssey.codeodysseyapi.common.exception;

public class SendEmailException extends RuntimeException {
    public SendEmailException(String message, Throwable cause) {
        super(message, cause);
    }
}
