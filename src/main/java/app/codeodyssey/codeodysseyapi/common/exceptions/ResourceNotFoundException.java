package app.codeodyssey.codeodysseyapi.common.exceptions;

import java.util.UUID;
import lombok.Getter;

@Getter
public class ResourceNotFoundException extends RuntimeException {
    private final ResourceName resourceName;
    private final UUID resourceId;

    public ResourceNotFoundException(ResourceName resourceName, UUID resourceId) {
        super();
        this.resourceName = resourceName;
        this.resourceId = resourceId;
    }
}
