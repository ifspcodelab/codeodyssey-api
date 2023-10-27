package app.codeodyssey.codeodysseyapi.resolution.api;

import app.codeodyssey.codeodysseyapi.course.api.CourseResponse;
import app.codeodyssey.codeodysseyapi.resolution.service.CreateResolutionCommand;
import app.codeodyssey.codeodysseyapi.resolution.service.CreateResolutionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/courses/{courseId}/activities/{activityId}/resolutions")
@AllArgsConstructor
public class CreateResolutionEndpoint {
    private final CreateResolutionService createResolutionService;

    @Operation(
            summary = "Create resolution.",
            description = "Register a new resolution on the database",
            tags = {"Resolution"})
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    content = {
                            @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = CourseResponse.class)),
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
                    responseCode = "409",
                    content = {
                            @Content(schema = @Schema(implementation = ProblemDetail.class), mediaType = "application/json")
                    })
    })
    @PostMapping
    public ResponseEntity<ResolutionResponse> post(
            @PathVariable UUID courseId, @PathVariable UUID activityId, @Valid @RequestBody CreateResolutionCommand command, Authentication authentication) {
        var userDetails = (UserDetails) authentication.getPrincipal();
        var username = userDetails.getUsername();
        return new ResponseEntity<>(createResolutionService.execute(courseId, activityId, command, username), HttpStatus.CREATED);
    }
}
