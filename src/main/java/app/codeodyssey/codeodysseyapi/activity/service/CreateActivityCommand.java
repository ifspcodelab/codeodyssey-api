package app.codeodyssey.codeodysseyapi.activity.service;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CreateActivityCommand(
        @NotNull @NotBlank @Size(min = 1, max = 255) String title,
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate,
        @NotNull Byte initialFile) {}
