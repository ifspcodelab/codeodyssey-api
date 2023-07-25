package app.codeodyssey.codeodysseyapi.user.util;

import app.codeodyssey.codeodysseyapi.token.util.AccessTokenFactory;
import app.codeodyssey.codeodysseyapi.token.util.AuthenticationTokenPair;
import app.codeodyssey.codeodysseyapi.token.util.RefreshTokenFactory;
import app.codeodyssey.codeodysseyapi.user.data.User;

public final class AuthenticationTokenPairFactory {
    private AuthenticationTokenPairFactory() {}

    public static AuthenticationTokenPair sampleAuthenticationTokenPair(User user) {
        var refreshToken = RefreshTokenFactory.createValidRefreshToken(user);
        var accessToken = AccessTokenFactory.sampleAccessToken(user);

        return new AuthenticationTokenPair(accessToken, refreshToken);
    }
}
