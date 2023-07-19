package app.codeodyssey.codeodysseyapi.user.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GetUserIdFromRegisterTokenService {
    public static UUID getUserId(String token) {
        String[] parts = token.split("\\|");

        if (parts.length != 2) {
            return null;
        }

        try {
            return UUID.fromString(parts[0]);

        } catch (IllegalArgumentException ex) {
            return null;
        }
    }
}
