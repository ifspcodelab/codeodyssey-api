package app.codeodyssey.codeodysseyapi.common.exceptions;

import lombok.Getter;

@Getter
public class ResourceAlreadyExistsException extends RuntimeException {
    private final ResourceName resourceName;
    private final String resourceAttribute;
    private final String resourceValue;

    public ResourceAlreadyExistsException(
            ResourceName resourceName, String resourceAttribute, String resourceValue) {
        super();
        this.resourceName = resourceName;
        this.resourceAttribute = resourceAttribute;
        this.resourceValue = resourceValue;
    }
}
