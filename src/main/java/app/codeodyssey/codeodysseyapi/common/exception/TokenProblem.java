package app.codeodyssey.codeodysseyapi.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TokenProblem {
    EXPIRED("Expired Token"),
    NONEXISTENT("No user associated with this token");

    private final String message;
}
