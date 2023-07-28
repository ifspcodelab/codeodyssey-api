package app.codeodyssey.codeodysseyapi.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Resource {
    COURSE("Course"),
    USER("User"),
    REFRESH_TOKEN("RefreshToken"),
    ACCESS_TOKEN("AccessToken");

    private final String name;
}
