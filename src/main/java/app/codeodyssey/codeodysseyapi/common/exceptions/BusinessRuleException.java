package app.codeodyssey.codeodysseyapi.common.exceptions;

import lombok.Getter;

@Getter
public class BusinessRuleException extends RuntimeException{
    private final BusinessRuleType businessRuleType;

    public BusinessRuleException(BusinessRuleType businessRuleType) {
        super();
        this.businessRuleType = businessRuleType;
    }
}
