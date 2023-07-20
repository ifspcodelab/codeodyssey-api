package app.codeodyssey.codeodysseyapi.course.data;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, UUID> {
    List<Course> findAllByOrderByNameAscEndDateAsc();

    List<Course> findAllByProfessorIdOrderByNameAscEndDateAsc(UUID id);

    @Query(
            """
            SELECT course
            FROM Enrollment enrollment
            JOIN enrollment.invitation invitation
            JOIN invitation.course course
            JOIN enrollment.student student
            WHERE student.id = :id
            ORDER BY course.name, course.endDate
    """)
    List<Course> findAllByStudentIdOrderByNameAscEndDateAsc(UUID id);
}
