package app.codeodyssey.codeodysseyapi.common.exceptions;

public class ResourceAlreadyExistsException extends RuntimeException{
    private final ResourceName resourceName;
    private final String resourceAttribute;
    private final String resourceAttributeValue;

    public ResourceAlreadyExistsException(ResourceName resourceName, String resourceAttribute, String resourceAttributeValue) {
        super();
        this.resourceName = resourceName;
        this.resourceAttribute = resourceAttribute;
        this.resourceAttributeValue = resourceAttributeValue;
    }
}
