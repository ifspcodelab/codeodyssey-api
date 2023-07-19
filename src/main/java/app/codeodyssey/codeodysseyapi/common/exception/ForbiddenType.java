package app.codeodyssey.codeodysseyapi.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ForbiddenType {
    EXPIRED_REFRESH_TOKEN("expired refresh token");

    private final String name;
}
