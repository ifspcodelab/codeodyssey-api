package app.codeodyssey.codeodysseyapi.common.exception.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Resource {
    USER("User");

    private final String name;
}
