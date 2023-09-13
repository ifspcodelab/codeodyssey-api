package app.codeodyssey.codeodysseyapi.invitation.api;

import app.codeodyssey.codeodysseyapi.invitation.service.GetInvitationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/invitations")
@AllArgsConstructor
public class GetInvitationEndpoint {
    private final GetInvitationService getInvitationService;

    @GetMapping("{invitationId}")
    public ResponseEntity<InvitationResponse> get(
            @PathVariable @Valid UUID invitationId) {
        return ResponseEntity.ok(getInvitationService.execute(invitationId));
    }
}
