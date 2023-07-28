package app.codeodyssey.codeodysseyapi.common.exception;

import lombok.Getter;

@Getter
public enum BusinessRuleType {
    COURSE_START_DATE_BEFORE_TODAY("start date is in the past"),
    COURSE_END_DATE_BEFORE_START_DATE("end date is earlier than its start date");

    String name;

    BusinessRuleType(String name) {
        this.name = name;
    }
}
