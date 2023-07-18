package app.codeodyssey.codeodysseyapi.invitation.data;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvitationRepository extends JpaRepository<Invitation, UUID> {}
