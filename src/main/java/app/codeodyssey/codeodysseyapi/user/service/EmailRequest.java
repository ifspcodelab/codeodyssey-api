package app.codeodyssey.codeodysseyapi.user.service;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record EmailRequest (
        @NotNull @NotEmpty @Email String email,
        @NotNull @NotEmpty String token
){}