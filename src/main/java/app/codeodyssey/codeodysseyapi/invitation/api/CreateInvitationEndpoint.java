package app.codeodyssey.codeodysseyapi.invitation.api;

import app.codeodyssey.codeodysseyapi.invitation.service.CreateInvitationService;
import app.codeodyssey.codeodysseyapi.invitation.service.InvitationCreateCommand;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/courses")
@AllArgsConstructor
@Tag(name = "Invitations", description = "Invitation API endpoint")
public class CreateInvitationEndpoint {
    private final CreateInvitationService createInvitationService;

    @Operation(
            summary = "Create invitations.",
            description = "Returns an invitation of a course given said course's id.",
            tags = {"Invitations"})
    @ApiResponses({
        @ApiResponse(
                responseCode = "201",
                content = {
                    @Content(
                            array = @ArraySchema(schema = @Schema(implementation = InvitationResponse.class)),
                            mediaType = "application/json")
                }),
        @ApiResponse(
                responseCode = "400",
                content = {
                        @Content(schema = @Schema(implementation = ProblemDetail.class), mediaType = "application/json")
                }),
        @ApiResponse(
                responseCode = "403",
                content = {
                        @Content(schema = @Schema(implementation = ProblemDetail.class), mediaType = "application/json")
                }),
        @ApiResponse(
                responseCode = "404",
                content = {
                        @Content(schema = @Schema(implementation = ProblemDetail.class), mediaType = "application/json")
                })
    })
    @PostMapping("{courseId}/invitations")
    public ResponseEntity<InvitationResponse> post(
            @PathVariable @Valid UUID courseId,
            @RequestBody @Valid InvitationCreateCommand command,
            Authentication authentication) {
        var userDetails = (UserDetails) authentication.getPrincipal();
        var username = userDetails.getUsername();
        return new ResponseEntity<>(createInvitationService.execute(command, courseId, username), HttpStatus.CREATED);
    }
}
