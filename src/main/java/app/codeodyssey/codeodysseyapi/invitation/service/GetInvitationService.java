package app.codeodyssey.codeodysseyapi.invitation.service;

import app.codeodyssey.codeodysseyapi.common.exception.*;
import app.codeodyssey.codeodysseyapi.invitation.api.InvitationResponse;
import app.codeodyssey.codeodysseyapi.invitation.data.Invitation;
import app.codeodyssey.codeodysseyapi.invitation.data.InvitationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class GetInvitationService {
    private final InvitationRepository invitationRepository;
    private final InvitationMapper invitationMapper;

    public InvitationResponse execute(UUID invitationId) {
        Invitation invitation = invitationRepository
                .findById(invitationId)
                .orElseThrow(() -> new ResourceNotFoundException(invitationId, Resource.USER));

        return invitationMapper.to(invitation);
    }
}
