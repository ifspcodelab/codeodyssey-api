package app.codeodyssey.codeodysseyapi.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@AllArgsConstructor
public class ValidateRegisterTokenService {
    public static boolean isTokenValid(String token) {
        String[] parts = token.split("\\|");

        long expirationTimeMillis = Long.parseLong(parts[1]);
        Instant expirationTime = Instant.ofEpochMilli(expirationTimeMillis);

        return Instant.now().isBefore(expirationTime);
    }
}