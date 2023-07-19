package app.codeodyssey.codeodysseyapi.user.service;

import app.codeodyssey.codeodysseyapi.common.exception.InvalidTokenException;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class ValidateRegisterTokenService {
    public static boolean isTokenValid(String token) {
        String[] parts = token.split("\\|");

        if (parts.length != 2) {
            throw new InvalidTokenException("Invalid token format.");
        }

        try {
            long expirationTimeMillis = Long.parseLong(parts[1]);
            Instant expirationTime = Instant.ofEpochMilli(expirationTimeMillis);

            return Instant.now().isBefore(expirationTime);

        } catch (NumberFormatException ex) {
            throw new InvalidTokenException("Invalid token format: Invalid expiration time.", ex);
        }
    }
}
