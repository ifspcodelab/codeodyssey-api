package app.codeodyssey.codeodysseyapi.common.exception;

import lombok.Getter;

@Getter
public class ResendEmailException extends RuntimeException {
    private final String email;

    public ResendEmailException(String email) {
        super();
        this.email = email;
    }
}
