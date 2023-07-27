package app.codeodyssey.codeodysseyapi.enrollment.service;

import app.codeodyssey.codeodysseyapi.invitation.data.Invitation;
import app.codeodyssey.codeodysseyapi.user.data.User;
import java.time.Instant;
import java.util.UUID;

public record EnrollmentResponse(UUID id, Invitation invitation, User student, Instant createdAt) {}
