package app.codeodyssey.codeodysseyapi.course.service;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record CreateUserCommand (
        @NotNull @NotBlank @Size(min = 1, max = 255) String name,
        @NotNull @NotBlank @Size(min = 1, max = 255) String slug,
        @NotNull LocalDate startDate,
        LocalDate endDate,
        @NotNull UUID professorId,
        Instant createdAt) {}
