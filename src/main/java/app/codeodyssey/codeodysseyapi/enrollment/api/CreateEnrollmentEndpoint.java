package app.codeodyssey.codeodysseyapi.enrollment.api;

import app.codeodyssey.codeodysseyapi.enrollment.service.CreateEnrollmentService;
import app.codeodyssey.codeodysseyapi.enrollment.service.EnrollmentResponse;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/invitations")
@AllArgsConstructor
@Tag(name = "Enrollments", description = "Enrollment API endpoint")
public class CreateEnrollmentEndpoint {
    private final CreateEnrollmentService createEnrollmentService;

    @Operation(
            summary = "Create enrollments.",
            description = "Returns an enrollment of a student on a course, given an invitation id of said course.",
            tags = {"Enrollments"})
    @ApiResponses({
        @ApiResponse(
                responseCode = "201",
                content = {
                    @Content(
                            array = @ArraySchema(schema = @Schema(implementation = EnrollmentResponse.class)),
                            mediaType = "application/json")
                })
    })
    @PostMapping("{invitationId}/enrollments")
    public ResponseEntity<EnrollmentResponse> post(
            @PathVariable @Valid UUID invitationId, Authentication authentication) {
        var userDetails = (UserDetails) authentication.getPrincipal();
        var username = userDetails.getUsername();
        return new ResponseEntity<>(createEnrollmentService.execute(invitationId, username), HttpStatus.CREATED);
    }
}
