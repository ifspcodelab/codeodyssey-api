package app.codeodyssey.codeodysseyapi.enrollment.data;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, UUID> {
    boolean existsByStudentIdAndInvitation_Course_Id(UUID studentId, UUID courseId);
}
