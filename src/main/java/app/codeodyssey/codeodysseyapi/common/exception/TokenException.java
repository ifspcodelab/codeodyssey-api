package app.codeodyssey.codeodysseyapi.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class TokenException extends RuntimeException {
    public TokenException(String message) {
        super(message);
    }
}
