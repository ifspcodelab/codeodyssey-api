package app.codeodyssey.codeodysseyapi.course.data;

import app.codeodyssey.codeodysseyapi.user.data.User;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, UUID> {
    boolean existsBySlugAndProfessor(String slug, User professor);
}
