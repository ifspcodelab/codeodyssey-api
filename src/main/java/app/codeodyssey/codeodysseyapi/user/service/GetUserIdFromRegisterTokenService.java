package app.codeodyssey.codeodysseyapi.user.service;

import app.codeodyssey.codeodysseyapi.common.exception.TokenMalformedException;
import app.codeodyssey.codeodysseyapi.common.exception.UserIdInvalidException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GetUserIdFromRegisterTokenService {
    public static UUID getUserId(String token) {
        String[] parts = token.split("\\|");

        if (parts.length != 2) {
            throw new TokenMalformedException("Token is malformed or invalid.");
        }

        try {
            return UUID.fromString(parts[0]);

        } catch (IllegalArgumentException ex) {
            throw new UserIdInvalidException("User ID in the token is not a valid UUID.");
        }
    }
}