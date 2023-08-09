package app.codeodyssey.codeodysseyapi.user.service;

import app.codeodyssey.codeodysseyapi.common.exception.*;
import app.codeodyssey.codeodysseyapi.course.data.Course;
import app.codeodyssey.codeodysseyapi.course.data.CourseRepository;
import app.codeodyssey.codeodysseyapi.user.api.UserResponse;
import app.codeodyssey.codeodysseyapi.user.data.User;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import app.codeodyssey.codeodysseyapi.user.data.UserRole;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GetCourseStudentsService {
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final UserMapper userMapper;

    public List<UserResponse> execute(UUID professorId, String courseSlug) {
        User professor = userRepository
                .findById(professorId)
                .orElseThrow(() -> new ResourceNotFoundException(professorId, Resource.USER));

        if (!professor.getRole().equals(UserRole.PROFESSOR)) {
            throw new ForbiddenAccessException(professorId);
        }

        Course course = courseRepository.findByProfessorIdAndCourseSlug(professorId, courseSlug);

        if (!courseRepository.existsBySlugAndProfessor(courseSlug, professor)) {
            throw new ViolationException(Resource.COURSE, ViolationType.COURSE_SLUG_NOT_FOUND, courseSlug);
        }

        return userMapper.to(userRepository.findUsersByCourseIdOrderByName(course.getId()));
    }
}
