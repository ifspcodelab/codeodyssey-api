package app.codeodyssey.codeodysseyapi.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TokenProblem {
    EXPIRED("Token Expired"),
    INEXISTENT("No user associated with this token");

    private final String message;
}
