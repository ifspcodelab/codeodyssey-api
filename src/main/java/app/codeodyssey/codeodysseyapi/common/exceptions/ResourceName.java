package app.codeodyssey.codeodysseyapi.common.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResourceName {
    COURSE("Course");

    private final String name;
}
