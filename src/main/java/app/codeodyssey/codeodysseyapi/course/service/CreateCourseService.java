package app.codeodyssey.codeodysseyapi.course.service;

import app.codeodyssey.codeodysseyapi.common.exceptions.*;
import app.codeodyssey.codeodysseyapi.course.api.CourseResponse;
import app.codeodyssey.codeodysseyapi.course.data.Course;
import app.codeodyssey.codeodysseyapi.course.data.CourseRepository;
import app.codeodyssey.codeodysseyapi.user.data.User;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreateCourseService {
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final UserRepository userRepository;

    public CourseResponse execute(UUID professorId, CreateCourseCommand command) {
        User professor = userRepository
                .findById(professorId)
                .orElseThrow(() -> new ResourceNotFoundException(ResourceName.USER, professorId));

        if (courseRepository.existsBySlugAndProfessor(command.slug(), professor)) {
            throw new ResourceAlreadyExistsException(ResourceName.COURSE, "slug", command.slug());
        }

        if (command.startDate().isBefore(LocalDate.now())) {
            throw new BusinessRuleException(BusinessRuleType.COURSE_START_DATE_BEFORE_TODAY);
        }

        if (command.endDate().isBefore(command.startDate())) {
            throw new BusinessRuleException(BusinessRuleType.COURSE_END_DATE_BEFORE_START_DATE);
        }

        Course course = courseRepository.save(
                new Course(command.name(), command.slug(), command.startDate(), command.endDate(), professor));

        return courseMapper.to(course);
    }
}