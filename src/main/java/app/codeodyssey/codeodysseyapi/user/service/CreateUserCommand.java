package app.codeodyssey.codeodysseyapi.user.service;

import app.codeodyssey.codeodysseyapi.user.data.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record CreateUserCommand(@NotNull @NotBlank String name,
                                @NotNull @Email String email,
                                @NotNull @NotBlank String password,
                                @NotNull UserRole role) {}
