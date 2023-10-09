package app.codeodyssey.codeodysseyapi.activity.service;

import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record CreateActivityCommand(
        @NotNull String title,
        @NotNull Instant startDate,
        @NotNull Instant endDate) {}
