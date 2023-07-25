package app.codeodyssey.codeodysseyapi.token.util;

import app.codeodyssey.codeodysseyapi.token.data.RefreshToken;

public class AuthenticationTokenPair {
    private String accessToken;
    private RefreshToken refreshToken;

    public AuthenticationTokenPair(String accessToken, RefreshToken refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public RefreshToken getRefreshToken() {
        return refreshToken;
    }
}
