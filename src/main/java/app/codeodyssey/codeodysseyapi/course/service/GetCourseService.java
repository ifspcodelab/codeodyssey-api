package app.codeodyssey.codeodysseyapi.course.service;

import app.codeodyssey.codeodysseyapi.common.exception.EmailNotFoundException;
import app.codeodyssey.codeodysseyapi.common.exception.ForbiddenAccessException;
import app.codeodyssey.codeodysseyapi.common.exception.Resource;
import app.codeodyssey.codeodysseyapi.common.exception.ResourceNotFoundException;
import app.codeodyssey.codeodysseyapi.course.api.CourseResponse;
import app.codeodyssey.codeodysseyapi.course.data.Course;
import app.codeodyssey.codeodysseyapi.course.data.CourseRepository;
import app.codeodyssey.codeodysseyapi.enrollment.data.Enrollment;
import app.codeodyssey.codeodysseyapi.user.data.User;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import app.codeodyssey.codeodysseyapi.user.data.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetCourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final CourseMapper courseMapper;

    public CourseResponse execute(UUID id, Authentication authentication) {

        Course course = this.courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id, Resource.COURSE));

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        User user = this.userRepository.findByEmail(username)
                .orElseThrow(() -> new EmailNotFoundException(username));

        if (!user.getRole().equals(UserRole.ADMIN)) {

            for (Enrollment e : user.getEnrollments()) {
                if (e.getInvitation().getCourse().getId().equals(course.getId())) {
                    return this.courseMapper.to(course);
                }

                if (course.getProfessor().getId().equals(user.getId())) {
                    return this.courseMapper.to(course);
                }
            }

            throw new ForbiddenAccessException(user.getId());
        }


        return this.courseMapper.to(course);
    }
}
