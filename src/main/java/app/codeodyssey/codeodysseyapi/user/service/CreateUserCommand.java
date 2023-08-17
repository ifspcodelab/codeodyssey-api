package app.codeodyssey.codeodysseyapi.user.service;

import app.codeodyssey.codeodysseyapi.common.validations.ValidPassword;
import jakarta.validation.constraints.*;

public record CreateUserCommand(
        @NotNull @NotEmpty @NotBlank @Size(min = 5, max = 100) String name,
        @NotNull @NotEmpty @Email String email,
        @ValidPassword String password) {}
