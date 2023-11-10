package app.codeodyssey.codeodysseyapi.activity.api;

import app.codeodyssey.codeodysseyapi.activity.service.CreateActivityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CreateActivityEndpoint {

    private final CreateActivityService createActivityService;

    @Operation(
            summary = "Create activity",
            description = "Register a new activity on the database",
            tags = {"Activities"})
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    content = {
                            @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = ActivityResponse.class)),
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
                    })
    })
    @PostMapping("/courses/{courseId}/activities")
    public ResponseEntity<ActivityResponse> post(@PathVariable UUID courseId,
                                                 @RequestBody @Valid ActivityRequest activity,
                                                 Authentication authentication) {

        return new ResponseEntity<>(
                this.createActivityService.execute(courseId, activity, authentication),
                HttpStatus.CREATED);

    }
}
