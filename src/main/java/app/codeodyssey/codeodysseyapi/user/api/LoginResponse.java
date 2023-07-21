package app.codeodyssey.codeodysseyapi.user.api;

import app.codeodyssey.codeodysseyapi.token.data.RefreshToken;

public record LoginResponse(String accessToken,
                            String refreshToken) {
}
