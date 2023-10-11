package app.codeodyssey.codeodysseyapi.common.exception;

import lombok.Getter;

import java.util.UUID;

@Getter
public class StudentNotEnrolledException extends RuntimeException {
    private final UUID studentId;
    private final UUID courseId;

    public StudentNotEnrolledException(UUID studentId, UUID courseId) {
        super();
        this.studentId = studentId;
        this.courseId = courseId;
    }
}
