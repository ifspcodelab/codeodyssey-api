package app.codeodyssey.codeodysseyapi.enrollment.service;

import app.codeodyssey.codeodysseyapi.invitation.api.InvitationResponse;
import app.codeodyssey.codeodysseyapi.user.api.UserResponse;
import java.time.Instant;
import java.util.UUID;

public record EnrollmentResponse(UUID id, InvitationResponse invitation, UserResponse student, Instant createdAt) {}
