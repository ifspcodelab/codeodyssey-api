package app.codeodyssey.codeodysseyapi.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UnauthorizedType {
    ACCESS_TOKEN_EXPIRED("access token expired");

    private final String name;
}
