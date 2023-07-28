package app.codeodyssey.codeodysseyapi.common.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.CONFLICT)
public class UserAlreadyValidatedException extends RuntimeException {
    public UserAlreadyValidatedException(String message) {
        super(message);
    }
}
