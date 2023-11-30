package app.codeodyssey.codeodysseyapi.resolution.api;

import app.codeodyssey.codeodysseyapi.resolution.service.GetResolutionsService;
import app.codeodyssey.codeodysseyapi.user.api.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/courses/{courseId}/activities/{activityId}/resolutions")
@AllArgsConstructor
public class GetResolutionsEndpoint {
    private final GetResolutionsService getResolutionsService;

    @Operation(
            summary = "Get resolution.",
            description = "Returns an resolution list when given an course id and activity id.",
            tags = {"Resolution"})
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = UserResponse.class)),
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
                    }),
            @ApiResponse(
                    responseCode = "409",
                    content = {
                            @Content(schema = @Schema(implementation = ProblemDetail.class), mediaType = "application/json")
                    })
    })
    @GetMapping
    public ResponseEntity<List<ResolutionResponse>> get(
            @PathVariable @Valid UUID courseId, @PathVariable @Valid UUID activityId, Authentication authentication) {
        var userDetails = (UserDetails) authentication.getPrincipal();
        var username = userDetails.getUsername();
        return ResponseEntity.ok(getResolutionsService.execute(courseId, activityId, username));
    }
}
