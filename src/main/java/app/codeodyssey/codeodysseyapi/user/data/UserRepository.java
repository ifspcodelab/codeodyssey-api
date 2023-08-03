package app.codeodyssey.codeodysseyapi.user.data;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);

    User getUserByEmail(String email);

    Optional<User> getUserByToken(String token);

    List<User> findByIsValidated(boolean isValidated);

    Optional<User> findByEmail(String email);

    @Query(
            """
            SELECT student
            FROM Enrollment enrollment
            JOIN enrollment.student student
            JOIN enrollment.invitation invitation
            JOIN invitation.course course
            WHERE course.id = :courseId
            ORDER BY student.name
    """)
    List<User> findUsersByCourseIdOrderByName(UUID courseId);
}
