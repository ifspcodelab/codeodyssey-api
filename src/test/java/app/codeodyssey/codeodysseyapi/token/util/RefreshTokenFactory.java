package app.codeodyssey.codeodysseyapi.token.util;

import app.codeodyssey.codeodysseyapi.token.data.RefreshToken;
import app.codeodyssey.codeodysseyapi.token.data.RefreshTokenStatus;
import app.codeodyssey.codeodysseyapi.user.data.User;
import java.time.Instant;
import java.util.UUID;

public final class RefreshTokenFactory {
    private RefreshTokenFactory() {}

    public static RefreshToken createValidRefreshToken(User user) {
        return new RefreshToken(UUID.randomUUID(), user, Instant.now(), RefreshTokenStatus.UNUSED, Instant.now());
    }
}
