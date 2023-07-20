package app.codeodyssey.codeodysseyapi.user.service;

import app.codeodyssey.codeodysseyapi.common.exception.InvalidTokenFormatException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@AllArgsConstructor
public class ValidateRegisterTokenService {
    public static boolean isTokenValid(String token) {
        String[] parts = token.split("\\|");

        if (parts.length != 2) {
            throw new InvalidTokenFormatException("Invalid token format", HttpStatus.BAD_REQUEST.value());
        }

        long expirationTimeMillis = Long.parseLong(parts[1]);
        Instant expirationTime = Instant.ofEpochMilli(expirationTimeMillis);

        return Instant.now().isBefore(expirationTime);
    }
}