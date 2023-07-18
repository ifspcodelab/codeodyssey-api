package app.codeodyssey.codeodysseyapi.user.service;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class ValidateTokenService {
    public static boolean isTokenValid(String token) {
        String[] parts = token.split("\\|");

        if (parts.length != 2) {
            return false;
        }

        UUID userId = UUID.fromString(parts[0]);
        long expirationTimeMillis = Long.parseLong(parts[1]);
        Instant expirationTime = Instant.ofEpochMilli(expirationTimeMillis);

        return userId != null && Instant.now().isBefore(expirationTime);
    }
}
