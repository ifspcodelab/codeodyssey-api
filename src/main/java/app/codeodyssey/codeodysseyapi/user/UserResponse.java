package app.codeodyssey.codeodysseyapi.user;

import java.time.Instant;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String name,
        String email,
        UserRole role,
        Instant createdAt) {}
