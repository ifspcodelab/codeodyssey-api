package app.codeodyssey.codeodysseyapi.token.util;

import app.codeodyssey.codeodysseyapi.token.data.RefreshToken;
import app.codeodyssey.codeodysseyapi.token.data.RefreshTokenStatus;
import app.codeodyssey.codeodysseyapi.user.data.User;

import java.time.Instant;
import java.util.UUID;

public class RefreshTokenFactory {
    public static RefreshToken createValidRefreshToken(User user){
        return new RefreshToken(
                UUID.randomUUID(),
                user,
                UUID.randomUUID().toString(),
                Instant.now(),
                RefreshTokenStatus.UNUSED
                );
    }

}
