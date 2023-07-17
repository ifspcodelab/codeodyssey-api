package app.codeodyssey.codeodysseyapi.course.api;

import app.codeodyssey.codeodysseyapi.user.api.UserResponse;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record CourseResponse(
        UUID id,
        String name,
        String slug,
        LocalDate startDate,
        LocalDate endDate,
        UserResponse professor,
        Instant createdAt) {}
