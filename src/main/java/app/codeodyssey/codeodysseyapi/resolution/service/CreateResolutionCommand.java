package app.codeodyssey.codeodysseyapi.resolution.service;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateResolutionCommand(
        Byte resolutionFile
        ) {}
