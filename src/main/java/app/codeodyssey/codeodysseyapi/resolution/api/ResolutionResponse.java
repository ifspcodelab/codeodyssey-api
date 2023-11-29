package app.codeodyssey.codeodysseyapi.resolution.api;

import app.codeodyssey.codeodysseyapi.activity.api.ActivityResponse;
import app.codeodyssey.codeodysseyapi.resolution.data.ResolutionStatus;
import app.codeodyssey.codeodysseyapi.user.api.UserResponse;

import java.time.Instant;
import java.util.UUID;

public record ResolutionResponse (
        UUID id,
        ActivityResponse activity,
        UserResponse student,
        Instant submitDate,
        String resolutionFile,
        ResolutionStatus status){}
