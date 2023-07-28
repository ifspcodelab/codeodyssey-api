package app.codeodyssey.codeodysseyapi.common.exception;

import java.util.UUID;
import lombok.Getter;

@Getter
public class StudentAlreadyEnrolledException extends RuntimeException {
    private final UUID studentId;
    private final UUID courseId;

    public StudentAlreadyEnrolledException(UUID studentId, UUID courseId) {
        super();
        this.studentId = studentId;
        this.courseId = courseId;
    }
}
