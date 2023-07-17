package app.codeodyssey.codeodysseyapi.course.api;

import app.codeodyssey.codeodysseyapi.user.data.User;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record CourseResponse(
        // TODO: change `User` to `UserResponse`
        UUID id, String name, String slug, LocalDate startDate, LocalDate endDate, User professor, Instant createdAt) {}
