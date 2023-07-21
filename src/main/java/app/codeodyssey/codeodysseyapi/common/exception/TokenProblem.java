package app.codeodyssey.codeodysseyapi.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TokenProblem {
    EXPIRED("Token Expired"),
    INEXISTENT("Token does not exist");

    private final String message;
}
