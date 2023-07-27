package app.codeodyssey.codeodysseyapi.invitation.api;

import app.codeodyssey.codeodysseyapi.invitation.service.CreateInvitationService;
import app.codeodyssey.codeodysseyapi.invitation.service.InvitationCreateCommand;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/courses")
@AllArgsConstructor
@Tag(name = "Invitation", description = "Invitation API endpoint")
public class CreateInvitationEndpoint {
    private final CreateInvitationService createInvitationService;

    @PostMapping("{courseId}/invitations")
    public ResponseEntity<InvitationResponse> post(
            @PathVariable @Valid UUID courseId,
            @RequestBody @Valid InvitationCreateCommand command,
            Authentication authentication) {
        System.out.println("hey @ controller");
        var userDetails = (UserDetails) authentication.getPrincipal();
        var username = userDetails.getUsername();
        return new ResponseEntity<>(createInvitationService.execute(command, courseId, username), HttpStatus.CREATED);
    }
}
