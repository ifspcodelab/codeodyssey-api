package app.codeodyssey.codeodysseyapi.invitation.service;

import app.codeodyssey.codeodysseyapi.invitation.api.InvitationResponse;
import app.codeodyssey.codeodysseyapi.invitation.data.InvitationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class GetInvitationsService {
    private final InvitationRepository invitationRepository;
    private final InvitationMapper invitationMapper;

    public List<InvitationResponse> execute(UUID courseId) {

        return invitationMapper.to(invitationRepository.findAllByCourseId(courseId));
    }
}
