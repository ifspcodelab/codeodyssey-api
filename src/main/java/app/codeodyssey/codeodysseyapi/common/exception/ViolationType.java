package app.codeodyssey.codeodysseyapi.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ViolationType {
    ALREADY_EXISTS("Already exists"),
    COURSE_START_DATE_BEFORE_TODAY("Start date is in the past"),
    COURSE_END_DATE_BEFORE_START_DATE("End date is earlier than its start date"),
    COURSE_SLUG_NOT_FOUND("Slug not found");

    private final String name;
}
