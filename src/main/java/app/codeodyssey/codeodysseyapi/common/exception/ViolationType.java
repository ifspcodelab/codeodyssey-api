package app.codeodyssey.codeodysseyapi.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ViolationType {
    ALREADY_EXISTS("already exists"),
    INCORRECT_CREDENTIALS("incorrect credentials");

    private final String name;
}