package app.codeodyssey.codeodysseyapi.user.service;

import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class CreateRegisterTokenService {
    public static String generateToken(UUID userId) {
        Instant expirationTime = Instant.now().plus(3, ChronoUnit.MINUTES);
        String token = String.format("%s|%s", userId.toString(), expirationTime.toEpochMilli());

        try {
            String encodedToken = URLEncoder.encode(token, StandardCharsets.UTF_8.toString());
            return encodedToken;

        } catch (Exception e) {
            return null;
        }
    }
}
