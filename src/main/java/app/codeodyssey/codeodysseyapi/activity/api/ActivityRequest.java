package app.codeodyssey.codeodysseyapi.activity.api;

import jakarta.validation.constraints.NotBlank;

import java.time.Instant;

public record ActivityRequest(@NotBlank String title,
                              @NotBlank String description,
                              @NotBlank Instant startDate,
                              @NotBlank Instant endDate,
                              @NotBlank byte[] initialFile,
                              @NotBlank byte[] solutionFile,
                              @NotBlank byte[] testFile,
                              @NotBlank String extension) {
}
