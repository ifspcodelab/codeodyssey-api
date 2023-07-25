package app.codeodyssey.codeodysseyapi.common.exception;

import lombok.Getter;

@Getter
public class BusinessRuleException extends RuntimeException {
    private final BusinessRuleType businessRuleType;
    private final Resource resource;
    private final String details;

    public BusinessRuleException(Resource resource, BusinessRuleType businessRuleType, String details) {
        super();
        this.resource = resource;
        this.businessRuleType = businessRuleType;
        this.details = details;
    }
}
