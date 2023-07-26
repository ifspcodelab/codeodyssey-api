package app.codeodyssey.codeodysseyapi.invitation.api;

import app.codeodyssey.codeodysseyapi.course.api.CourseResponse;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record InvitationResponse(
        UUID id,
        String link,
        LocalDate expirationDate,
        CourseResponse course,
        Instant createdAt
) {}
