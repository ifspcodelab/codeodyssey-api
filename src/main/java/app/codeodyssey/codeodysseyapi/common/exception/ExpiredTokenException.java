package app.codeodyssey.codeodysseyapi.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class ExpiredTokenException extends RuntimeException {
    public  ExpiredTokenException(String message) {
        super(message);
    }
}
