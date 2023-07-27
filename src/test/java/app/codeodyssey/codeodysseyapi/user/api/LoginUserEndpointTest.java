package app.codeodyssey.codeodysseyapi.user.api;

import app.codeodyssey.codeodysseyapi.common.security.JwtService;
import app.codeodyssey.codeodysseyapi.token.data.RefreshToken;
import app.codeodyssey.codeodysseyapi.token.service.GetTokenService;
import app.codeodyssey.codeodysseyapi.token.util.RefreshTokenFactory;
import app.codeodyssey.codeodysseyapi.user.data.User;
import app.codeodyssey.codeodysseyapi.user.util.UserFactory;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.time.DateUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Testcontainers;

@ExtendWith(SpringExtension.class)
@Testcontainers
@DisplayName("tests for login endpoint")
class LoginUserEndpointTest {

    @InjectMocks
    private LoginUserEndpoint loginUserEndpoint;

    @Mock
    private GetTokenService getTokenService;

    @Mock
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        User user = UserFactory.createValidUser();
        RefreshToken refreshToken = RefreshTokenFactory.createValidRefreshToken(user);
        Map<String, Object> userClaims = new HashMap<>();
        userClaims.put("role", user.getRole().toString());
        String rawKey = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

        String accessToken = Jwts.builder()
                .setClaims(userClaims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(DateUtils.addMinutes(new Date(), 15))
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(rawKey)), SignatureAlgorithm.HS256)
                .compact();

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode(rawKey)))
                .build()
                .parseClaimsJws(accessToken)
                .getBody();

        LoginResponse loginResponse = new LoginResponse(accessToken, refreshToken.getToken());
        BDDMockito.when(getTokenService.execute(ArgumentMatchers.any())).thenReturn(loginResponse);
        BDDMockito.when(jwtService.extractAllClaims(ArgumentMatchers.any())).thenReturn(claims);
    }

    @Test
    @DisplayName("returns a jwt when successful")
    void login_givenLoginRequest_returnsLoginResponse() {
        User user = UserFactory.createValidUser();
        LoginResponse loginResponse = loginUserEndpoint.login(null).getBody();
        assert loginResponse != null;
        Claims claims = jwtService.extractAllClaims(loginResponse.accessToken());
        Assertions.assertThat(claims.getSubject()).isEqualTo(user.getEmail());
        Assertions.assertThat(claims.getExpiration()).isBefore(DateUtils.addMinutes(new Date(), 16));
    }
}
