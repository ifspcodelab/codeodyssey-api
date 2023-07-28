package app.codeodyssey.codeodysseyapi.course.service;

import app.codeodyssey.codeodysseyapi.common.exception.*;
import app.codeodyssey.codeodysseyapi.course.api.CourseResponse;
import app.codeodyssey.codeodysseyapi.course.data.Course;
import app.codeodyssey.codeodysseyapi.course.data.CourseRepository;
import app.codeodyssey.codeodysseyapi.user.data.User;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import app.codeodyssey.codeodysseyapi.user.data.UserRole;
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
                .orElseThrow(() -> new ResourceNotFoundException(professorId, Resource.USER));

        if (!professor.getRole().equals(UserRole.PROFESSOR)) {
            throw new ForbiddenAccessException(professorId);
        }

        if (courseRepository.existsBySlugAndProfessor(command.slug(), professor)) {
            throw new ViolationException(Resource.COURSE, ViolationType.ALREADY_EXISTS, command.slug());
        }

        if (command.startDate().isBefore(LocalDate.now())) {
            throw new BusinessRuleException(
                    Resource.COURSE,
                    BusinessRuleType.COURSE_START_DATE_BEFORE_TODAY,
                    command.startDate().toString());
        }

        if (command.endDate().isBefore(command.startDate())) {
            throw new BusinessRuleException(
                    Resource.COURSE,
                    BusinessRuleType.COURSE_END_DATE_BEFORE_START_DATE,
                    command.endDate().toString());
        }

        Course course = courseRepository.save(
                new Course(command.name(), command.slug(), command.startDate(), command.endDate(), professor));

        return courseMapper.to(course);
    }
}
