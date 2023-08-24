package app.codeodyssey.codeodysseyapi.user.api;

import app.codeodyssey.codeodysseyapi.user.data.UserRole;
import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponse(UUID id, String name, String email, UserRole role, LocalDateTime createdAt) {}
