package app.codeodyssey.codeodysseyapi.invitation.service;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record InvitationCreateCommand(@NotNull @NotBlank LocalDate expirationDate) {}
