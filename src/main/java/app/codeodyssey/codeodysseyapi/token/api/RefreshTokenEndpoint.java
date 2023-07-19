package app.codeodyssey.codeodysseyapi.token.api;

import app.codeodyssey.codeodysseyapi.common.exception.ForbiddenException;
import app.codeodyssey.codeodysseyapi.common.exception.ForbiddenType;
import app.codeodyssey.codeodysseyapi.common.exception.Resource;
import app.codeodyssey.codeodysseyapi.common.security.JwtService;
import app.codeodyssey.codeodysseyapi.token.data.RefreshToken;
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

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Refresh Token", description = "Refresh Token API endpoint")
public class RefreshTokenEndpoint {

    private final JwtService jwtService;

    @Operation(
            summary = "Use refresh token to generate a new access token",
            description = "Returns the refresh token and the new access token",
            tags = {"Users"})
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(
                                    schema = @Schema(implementation = RefreshTokenResponse.class),
                                    mediaType = "application/json")
                    }),
            @ApiResponse(
                    responseCode = "403",
                    content = {
                            @Content(
                                    schema = @Schema(implementation = ProblemDetail.class),
                                    mediaType = "application/json")
                    })
    })
    @CrossOrigin(origins = "*")
    @PostMapping("/refreshtoken")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@RequestBody RefreshTokenRequest request){
        String refreshToken = request.refreshToken();
        Map<String, Object> claims = new HashMap<>();

        return jwtService.findByToken(refreshToken)
                .map(jwtService::verifyRefreshTokenUsed)
                .map(jwtService::verifyRefreshTokenExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    claims.put("role", user.getRole());
                    String accessToken = jwtService.generateAccessToken(claims, user);
                    return new ResponseEntity<>(new RefreshTokenResponse(
                            this.jwtService.generateRefreshToken(user.getId(), refreshToken).getToken(),
                            accessToken),
                            HttpStatus.CREATED);
                })
                .orElseThrow(() -> new ForbiddenException(Resource.REFRESH_TOKEN,
                        ForbiddenType.REFRESH_TOKEN_NOT_FOUND,
                        "refresh token was not found"));
    }

}
