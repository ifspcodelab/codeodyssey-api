package app.codeodyssey.codeodysseyapi.resolution.service;

import jakarta.validation.constraints.NotNull;

public record CreateResolutionCommand(
        @NotNull String resolutionFile) {}
