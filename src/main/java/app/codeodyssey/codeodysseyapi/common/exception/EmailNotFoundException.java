package app.codeodyssey.codeodysseyapi.common.exception;

import lombok.Getter;

@Getter
public class EmailNotFoundException extends RuntimeException {
    private final String email;

    public EmailNotFoundException(String email) {
        super();
        this.email = email;
    }
}
