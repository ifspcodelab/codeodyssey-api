package app.codeodyssey.codeodysseyapi.invitation.data;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, UUID> {
    @Query(
            """
            SELECT invitation
            FROM Invitation invitation
            WHERE invitation.course.id = :courseId
            ORDER BY invitation.createdAt DESC LIMIT 1
    """)
    Invitation findByCourseId(UUID courseId);
}
