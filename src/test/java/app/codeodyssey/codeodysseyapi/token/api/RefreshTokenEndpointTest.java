package app.codeodyssey.codeodysseyapi.token.api;

import app.codeodyssey.codeodysseyapi.common.security.JwtService;
import app.codeodyssey.codeodysseyapi.token.data.RefreshToken;
import app.codeodyssey.codeodysseyapi.token.util.RefreshTokenFactory;
import app.codeodyssey.codeodysseyapi.user.data.User;
import app.codeodyssey.codeodysseyapi.user.util.UserFactory;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("tests for refresh token endpoint")
class RefreshTokenEndpointTest {

    @MockBean
    private JwtService jwtService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("returns a refresh token and access token when successful")
    void refreshToken_givenAValidRefreshToken_returnsRefreshTokenResponse() throws Exception {
        User user = UserFactory.createValidUser();
        RefreshToken refreshToken = RefreshTokenFactory.createValidRefreshToken(user);

        Map<String, Object> userClaims = new HashMap<>();
        userClaims.put("role", user.getRole().toString());
        String rawKey = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

        String postPayload = """
                {
                "refreshToken":"%s"
                }
                """.formatted(refreshToken.getToken());

        RefreshToken newRefreshToken = RefreshTokenFactory.createValidRefreshToken(user);

        String accessToken = Jwts
                .builder()
                .setClaims(userClaims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(DateUtils.addMinutes(new Date(), 15))
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(rawKey)), SignatureAlgorithm.HS256)
                .compact();

        when(jwtService.findByToken(ArgumentMatchers.any()))
                .thenReturn(Optional.of(refreshToken));
        when(jwtService.verifyRefreshTokenUsed(ArgumentMatchers.any()))
                .thenReturn(refreshToken);
        when(jwtService.verifyRefreshTokenExpiration(ArgumentMatchers.any()))
                .thenReturn(refreshToken);
        when(jwtService.generateAccessToken(ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(accessToken);
        when(jwtService.generateRefreshToken(ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(newRefreshToken);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api/v1/refreshtoken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postPayload)
                ).andExpect(
                        MockMvcResultMatchers.status().isCreated()
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.refreshToken")
                                .value(newRefreshToken.getToken())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.accessToken")
                                .value(accessToken)
                );
    }

}