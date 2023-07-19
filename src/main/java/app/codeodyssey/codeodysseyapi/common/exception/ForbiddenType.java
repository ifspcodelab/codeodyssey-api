package app.codeodyssey.codeodysseyapi.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ForbiddenType {
    EXPIRED_REFRESH_TOKEN("expired refresh token"),
    REFRESH_TOKEN_NOT_FOUND("refresh token not found");

    private final String name;
}
