package app.codeodyssey.codeodysseyapi.common.exception;

import java.util.UUID;
import lombok.Getter;

@Getter
public class ResourceNotFoundException extends RuntimeException {
    private final UUID id;
    private final Resource resource;

    public ResourceNotFoundException(UUID id, Resource resource) {
        super();
        this.id = id;
        this.resource = resource;
    }
}