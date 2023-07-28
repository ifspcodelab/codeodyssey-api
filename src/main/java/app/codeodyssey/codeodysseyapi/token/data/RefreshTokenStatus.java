package app.codeodyssey.codeodysseyapi.token.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RefreshTokenStatus {
    UNUSED("UNUSED"),
    USED("USED"),
    BLOCKED("BLOCKED");

    private final String name;
}
