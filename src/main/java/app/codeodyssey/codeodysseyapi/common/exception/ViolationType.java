package app.codeodyssey.codeodysseyapi.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ViolationType {
    ALREADY_EXISTS("Already exists"),
    COURSE_START_DATE_BEFORE_TODAY("Start date is in the past"),
    COURSE_END_DATE_BEFORE_START_DATE("End date is earlier than its start date"),
    COURSE_SLUG_NOT_FOUND("Slug not found"),
    INVITATION_EXPIRATION_DATE_BEFORE_TODAY("Expiration date is in the past"),
    INVITATION_EXPIRATION_DATE_AFTER_COURSE_END_DATE("Expiration date is earlier than the course end date");

    private final String name;
}
