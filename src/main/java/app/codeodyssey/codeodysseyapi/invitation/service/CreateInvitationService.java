package app.codeodyssey.codeodysseyapi.invitation.service;

import app.codeodyssey.codeodysseyapi.invitation.api.InvitationResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreateInvitationService {

    public InvitationResponse execute(InvitationCreateCommand command) {
        return null;
    }
}
