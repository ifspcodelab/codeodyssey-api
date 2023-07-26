package app.codeodyssey.codeodysseyapi.user.service;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateUserCommand(
        @NotNull @NotBlank @Size(min = 5, max = 100) String name,
        @NotNull String email,
        @NotNull String password) {}
