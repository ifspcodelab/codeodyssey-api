package app.codeodyssey.codeodysseyapi.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserNotFoundException extends RuntimeException {
    private String message;
    private Resource resource;

    public UserNotFoundException(String email) {
        this.message = "user with email " + email + " not found";
        this.resource = Resource.USER;
    }
}
