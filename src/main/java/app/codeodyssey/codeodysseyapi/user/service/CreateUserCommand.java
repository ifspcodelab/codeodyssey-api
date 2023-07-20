package app.codeodyssey.codeodysseyapi.user.service;

import app.codeodyssey.codeodysseyapi.validations.ValidEmail;
import app.codeodyssey.codeodysseyapi.validations.ValidPassword;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.*;

public record CreateUserCommand(
        @NotNull @NotBlank @Size(min = 5, max = 100) String name,
        @NotNull @Size @NotEmpty @ValidEmail String email,
        @NotNull @ValidPassword String password,
        @JsonIgnore String hashedPassword) {}
