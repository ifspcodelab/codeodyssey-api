package app.codeodyssey.codeodysseyapi.user.api;

import app.codeodyssey.codeodysseyapi.user.data.UserRole;
import java.time.Instant;
import java.util.UUID;

public record UserResponse(UUID id, String email, String name, UserRole role, Instant createdAt) {}
