package app.codeodyssey.codeodysseyapi.course;

import app.codeodyssey.codeodysseyapi.course.api.CourseCreateDTO;
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

    public Course create(CourseCreateDTO courseCreateDto) {
        User professor = userService.findById(courseCreateDto.getProfessorId());

        Course course = new Course(
                courseCreateDto.getName(),
                courseCreateDto.getSlug(),
                courseCreateDto.getStartDate(),
                courseCreateDto.getEndDate(),
                professor
        );
        return courseRepository.save(course);
    }
}
