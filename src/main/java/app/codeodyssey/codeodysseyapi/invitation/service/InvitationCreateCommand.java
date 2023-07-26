package app.codeodyssey.codeodysseyapi.invitation.service;

import java.time.LocalDate;

public record InvitationCreateCommand(
   LocalDate expirationDate
) {}
