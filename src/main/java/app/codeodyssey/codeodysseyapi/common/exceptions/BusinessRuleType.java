package app.codeodyssey.codeodysseyapi.common.exceptions;

import lombok.Getter;

@Getter
public enum BusinessRuleType {
    COURSE_START_DATE_BEFORE_TODAY("The start date of the course is in the past"),
    COURSE_END_DATE_BEFORE_START_DATE("The end date of the course is earlier than its start date");

    String message;

    BusinessRuleType(String message) {
        this.message = message;
    }
}
