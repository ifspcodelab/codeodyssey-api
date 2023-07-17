package app.codeodyssey.codeodysseyapi.user.api;

import app.codeodyssey.codeodysseyapi.user.data.UserRole;
import java.time.Instant;
import java.util.UUID;

public record UserResponse(String name, String email, UUID password, UserRole role, Instant createdAt) { }
