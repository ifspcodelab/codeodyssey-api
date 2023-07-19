package app.codeodyssey.codeodysseyapi.user.service;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class CreateRegisterTokenService {
    public static String generateToken(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID canno be null to generate a token");
        }

        Instant expirationTime = Instant.now().plus(3, ChronoUnit.MINUTES);

        return String.format("%s|%s", userId, expirationTime.toEpochMilli());
    }
}
