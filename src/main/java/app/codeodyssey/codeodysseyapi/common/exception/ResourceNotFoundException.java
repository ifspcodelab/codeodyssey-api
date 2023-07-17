package app.codeodyssey.codeodysseyapi.common.exception;

import lombok.Getter;

import java.util.UUID;

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
