package app.codeodyssey.codeodysseyapi.user.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateUserCommand(@NotNull @NotBlank String name,
                                @NotNull @Email String email,
                                @NotNull @NotBlank String password,
                                @JsonIgnore String hashedPassword) {}
