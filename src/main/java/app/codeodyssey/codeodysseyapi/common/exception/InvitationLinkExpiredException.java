package app.codeodyssey.codeodysseyapi.common.exception;

import lombok.Getter;

import java.util.UUID;

@Getter
public class InvitationLinkExpiredException extends RuntimeException {
    private final UUID invitationId;
    private final UUID courseId;

    public InvitationLinkExpiredException(UUID invitationId, UUID courseId) {
        super();
        this.invitationId = invitationId;
        this.courseId = courseId;
    }
}
