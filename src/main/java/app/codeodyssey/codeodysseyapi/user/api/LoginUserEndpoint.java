package app.codeodyssey.codeodysseyapi.user.api;

import app.codeodyssey.codeodysseyapi.token.service.GetTokenService;
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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Login", description = "Login API endpoint")
public class LoginUserEndpoint {

    private final GetTokenService getTokenService;

    @Operation(
            summary = "Log in into the platform",
            description = "Returns a JWT containing an access token and a refresh token")
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                content = {
                    @Content(schema = @Schema(implementation = LoginResponse.class), mediaType = "application/json")
                }),
        @ApiResponse(
                responseCode = "403",
                content = {
                    @Content(schema = @Schema(implementation = ProblemDetail.class), mediaType = "application/json")
                }),
        @ApiResponse(
                responseCode = "400",
                content = {
                    @Content(schema = @Schema(implementation = ProblemDetail.class), mediaType = "application/json")
                })
    })
    @CrossOrigin(origins = "*")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return new ResponseEntity<>(getTokenService.execute(loginRequest), HttpStatus.OK);
    }
}
