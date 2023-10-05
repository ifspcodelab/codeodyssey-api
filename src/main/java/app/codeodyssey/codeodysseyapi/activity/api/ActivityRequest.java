package app.codeodyssey.codeodysseyapi.activity.api;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record ActivityRequest(@NotBlank String title,
                              @NotBlank String description,
                              @NotNull @Future Instant startDate,
                              @NotNull @Future Instant endDate,
                              @NotBlank String initialFile,
                              @NotBlank String solutionFile,
                              @NotBlank String testFile,
                              @NotBlank String extension) {
}
