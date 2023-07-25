package app.codeodyssey.codeodysseyapi.common.exceptions;

import java.util.UUID;

import lombok.Getter;

@Getter
public class ResourceNotFoundException extends RuntimeException {
    private final UUID id;
    private final Resource resource;

    public ResourceNotFoundException(UUID id, Resource resource) {
        super(resource + " not found with id " + id);
        this.id = id;
        this.resource = resource;
    }
}
