package app.codeodyssey.codeodysseyapi.course.service;

import app.codeodyssey.codeodysseyapi.common.exception.EmailNotFoundException;
import app.codeodyssey.codeodysseyapi.common.exception.ForbiddenAccessException;
import app.codeodyssey.codeodysseyapi.course.api.CourseResponse;
import app.codeodyssey.codeodysseyapi.course.data.CourseRepository;
import app.codeodyssey.codeodysseyapi.user.data.User;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import app.codeodyssey.codeodysseyapi.role.data.RoleType;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GetCoursesService {
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final CourseMapper courseMapper;

    public List<CourseResponse> execute(String userEmail) {
        Optional<User> user = userRepository.findByEmail(userEmail);

        if (user.isEmpty()) {
            throw new EmailNotFoundException(userEmail);
        }

        if (!user.get().getAuthorities().contains(new SimpleGrantedAuthority(RoleType.ADMIN.name()))) {
            throw new ForbiddenAccessException(user.get().getId());
        }

        return courseMapper.to(courseRepository.findAllByOrderByNameAscEndDateAsc());
    }
}
