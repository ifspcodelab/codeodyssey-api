package app.codeodyssey.codeodysseyapi.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    private final Resource resource;
    private final UUID id;

    public ResourceNotFoundException(UUID id, Resource resource) {
        super(resource + " not found with id " + id);
        this.id = id;
        this.resource = resource;
    }
}
