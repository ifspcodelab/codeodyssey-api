package app.codeodyssey.codeodysseyapi.course.service;

import app.codeodyssey.codeodysseyapi.course.data.Course;
import app.codeodyssey.codeodysseyapi.course.data.CourseRepository;
import app.codeodyssey.codeodysseyapi.user.User;
import app.codeodyssey.codeodysseyapi.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final UserService userService;

    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    public Course create(CreateUserCommand command) {
        User professor = userService.findById(command.getProfessorId());

        Course course = new Course(
                command.getName(),
                command.getSlug(),
                command.getStartDate(),
                command.getEndDate(),
                professor
        );
        return courseRepository.save(course);
    }
}
