package app.codeodyssey.codeodysseyapi.activity.service;

import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record CreateActivityCommand(
        @NotNull String title,
        @NotNull String description,
        @NotNull Instant startDate,
        @NotNull Instant endDate,
        @NotNull String initialFile,
        @NotNull String solutionFile,
        @NotNull String testFile,
        @NotNull String extension) {}
