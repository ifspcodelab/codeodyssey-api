package app.codeodyssey.codeodysseyapi.activity.service;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateActivityCommand(
        @NotNull String title,
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate) {}
