package app.codeodyssey.codeodysseyapi.token.api;

public record RefreshTokenResponse(String requestRefreshToken,
                                   String accessToken) {
}
