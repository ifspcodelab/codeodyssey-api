package app.codeodyssey.codeodysseyapi.common.exception;

import lombok.Getter;

import java.util.UUID;

@Getter
public class UserNotAssociatedException extends RuntimeException {
    private final UUID userId;
    private final UUID courseId;

    public UserNotAssociatedException(UUID userId, UUID courseId) {
        super();
        this.userId = userId;
        this.courseId = courseId;
    }
}
