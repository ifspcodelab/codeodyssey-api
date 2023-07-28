package app.codeodyssey.codeodysseyapi.course.service;

import app.codeodyssey.codeodysseyapi.common.exception.EmailNotFoundException;
import app.codeodyssey.codeodysseyapi.common.exception.ForbiddenAccessException;
import app.codeodyssey.codeodysseyapi.course.api.CourseResponse;
import app.codeodyssey.codeodysseyapi.course.data.CourseRepository;
import app.codeodyssey.codeodysseyapi.user.data.User;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import app.codeodyssey.codeodysseyapi.user.data.UserRole;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GetStudentCoursesService {
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final CourseMapper courseMapper;

    public List<CourseResponse> execute(UUID id, String userEmail) {
        Optional<User> user = userRepository.findByEmail(userEmail);

        if (user.isEmpty()) {
            throw new EmailNotFoundException(userEmail);
        }

        if (!user.get().getRole().equals(UserRole.STUDENT)) {
            throw new ForbiddenAccessException(user.get().getId());
        }

        if (!user.get().getId().equals(id)) {
            throw new ForbiddenAccessException(user.get().getId());
        }

        return courseMapper.to(courseRepository.findAllByStudentIdOrderByNameAscEndDateAsc(id));
    }
}
