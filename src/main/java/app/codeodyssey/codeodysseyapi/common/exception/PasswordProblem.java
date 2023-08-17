package app.codeodyssey.codeodysseyapi.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PasswordProblem {
    EMPTY("The passowrd cannot be empty"),
    NULL("The password cannot be null"),
    MIN_LENGTH("The password must have 8 characters at minimum"),
    MAX_LENGTH("The password must have 64 characters at maximum"),
    PATTERN("The password must have at least one uppercase letter, one lowercase letter, one number and one special character");

    private final String message;
}
