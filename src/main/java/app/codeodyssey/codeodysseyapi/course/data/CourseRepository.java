package app.codeodyssey.codeodysseyapi.course.data;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, UUID> {
    List<Course> findAllByOrderByNameAscEndDateAsc();

    List<Course> findAllByProfessorIdOrderByNameAscEndDateAsc(UUID id);
}
