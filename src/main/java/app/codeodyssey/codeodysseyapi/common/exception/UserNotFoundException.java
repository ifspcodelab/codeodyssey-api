package app.codeodyssey.codeodysseyapi.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserNotFoundException extends RuntimeException {
    private String message;
    private Resource resource;
}
