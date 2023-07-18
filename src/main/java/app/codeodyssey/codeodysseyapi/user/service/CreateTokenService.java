package app.codeodyssey.codeodysseyapi.user.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CreateTokenService {
    public static String generateToken() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "");
    }
}
