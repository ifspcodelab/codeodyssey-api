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
    INVITATION_EXPIRATION_DATE_AFTER_COURSE_END_DATE("Expiration date is earlier than the course end date"),
    ENROLLMENT_PROFESSOR_WHO_CREATED_COURSE_CANNOT_BE_ENROLLED("professor who created the course cannot be enrolled in it"),
    RESOLUTION_SUBMIT_DATE_BEFORE_ACTIVITY_STAR_DATE("Submit date is earlier than its activity start date"),
    RESOLUTION_SUBMIT_DATE_AFTER_ACTIVITY_END_DATE("Submit date later than its activity end date"),
    ACTIVITY_IS_NOT_FROM_COURSE("Activity is not from that course");

    private final String name;
}
