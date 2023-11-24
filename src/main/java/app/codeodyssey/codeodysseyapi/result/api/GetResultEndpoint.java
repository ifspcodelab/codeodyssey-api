package app.codeodyssey.codeodysseyapi.result.api;

import app.codeodyssey.codeodysseyapi.result.service.GetResultService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/activities")
@RequiredArgsConstructor
@Tag(name = "Results", description = "Results api endpoint")
public class GetResultEndpoint {

    private final GetResultService getActivityResultsService;

    @Operation(
            summary = "Get a result and its test cases",
            description = "Returns a result based on a resolution id",
            tags = {"Results"})
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(
                                    schema = @Schema(implementation = ResultResponse.class),
                                    mediaType = "application/json")
                    }),
            @ApiResponse(
                    responseCode = "403",
                    content = {
                            @Content(schema = @Schema(implementation = ProblemDetail.class), mediaType = "application/json")
                    })
    })
    @GetMapping("/{activityId}/resolutions/{resolutionId}/results")
    public ResponseEntity<ResultResponse> get(@PathVariable UUID activityId,
                                              @PathVariable UUID resolutionId,
                                              Authentication auth) {
        UserDetails principal = (UserDetails) auth.getPrincipal();
        return new ResponseEntity<>(
                getActivityResultsService.execute(activityId, resolutionId, principal.getUsername()), HttpStatus.OK
        );
    }
}
