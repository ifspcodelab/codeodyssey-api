package app.codeodyssey.codeodysseyapi.token.api;

public record RefreshTokenResponse(String refreshToken,
                                   String accessToken) {
}
