package app.codeodyssey.codeodysseyapi.user.api;

import app.codeodyssey.codeodysseyapi.role.data.Role;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record UserResponse(UUID id, String name, String email, List<Role> roles, Instant createdAt) {}
