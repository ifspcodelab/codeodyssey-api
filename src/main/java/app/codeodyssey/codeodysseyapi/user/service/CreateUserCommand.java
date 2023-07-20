package app.codeodyssey.codeodysseyapi.user.service;

import app.codeodyssey.codeodysseyapi.validations.CustomEmail;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.*;

public record CreateUserCommand(
        @NotNull @NotBlank @Size(min = 5, max = 100) String name,
        @NotNull @Size @NotEmpty @CustomEmail String email,
        @NotNull @NotBlank @Size(min = 8, max = 64) @NotEmpty String password,
        @JsonIgnore String hashedPassword) {}
