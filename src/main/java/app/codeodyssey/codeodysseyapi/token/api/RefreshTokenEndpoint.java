package app.codeodyssey.codeodysseyapi.token.api;

import app.codeodyssey.codeodysseyapi.common.exception.ForbiddenException;
import app.codeodyssey.codeodysseyapi.common.exception.ForbiddenType;
import app.codeodyssey.codeodysseyapi.common.exception.Resource;
import app.codeodyssey.codeodysseyapi.common.security.JwtService;
import app.codeodyssey.codeodysseyapi.token.data.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class RefreshTokenEndpoint {

    private final JwtService jwtService;

    @PostMapping("/refreshtoken")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@RequestBody RefreshTokenRequest request){
        String refreshToken = request.refreshToken();
        Map<String, Object> claims = new HashMap<>();

        return jwtService.findByToken(refreshToken)
                .map(jwtService::verifyRefreshTokenExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    claims.put("role", user.getRole());
                    String accessToken = jwtService.generateAccessToken(claims, user);
                    return new ResponseEntity<>(new RefreshTokenResponse(refreshToken, accessToken),
                            HttpStatus.CREATED);
                })
                .orElseThrow(() -> new ForbiddenException(Resource.REFRESH_TOKEN,
                        ForbiddenType.REFRESH_TOKEN_NOT_FOUND,
                        "refresh token was not found"));
    }

}
