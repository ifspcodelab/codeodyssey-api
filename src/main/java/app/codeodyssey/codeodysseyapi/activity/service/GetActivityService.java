package app.codeodyssey.codeodysseyapi.activity.service;

import app.codeodyssey.codeodysseyapi.activity.api.ActivityResponse;
import app.codeodyssey.codeodysseyapi.activity.data.ActivityRepository;
import app.codeodyssey.codeodysseyapi.common.exception.*;
import app.codeodyssey.codeodysseyapi.course.data.CourseRepository;
import app.codeodyssey.codeodysseyapi.enrollment.data.EnrollmentRepository;
import app.codeodyssey.codeodysseyapi.user.data.User;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import app.codeodyssey.codeodysseyapi.user.data.UserRole;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class GetActivityService {
    private final ActivityMapper activityMapper;
    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;

    public ActivityResponse execute(UUID courseId, UUID activityId, String userEmail) {
        Optional<User> user = userRepository.findByEmail(userEmail);

        if (user.isEmpty()) {
            throw new EmailNotFoundException(userEmail);
        }

        if (!activityRepository.existsByCourseIdAndId(courseId, activityId)) {
            throw new ViolationException(Resource.ACTIVITY, ViolationType.ACTIVITY_IS_NOT_FROM_COURSE, activityId.toString());
        }

        if (user.get().getRole().equals(UserRole.STUDENT)){
            if (!enrollmentRepository.existsByStudentIdAndInvitation_Course_Id(user.get().getId(), courseId)) {
                throw new StudentNotEnrolledException(user.get().getId(), courseId);
            }
        }

        if (user.get().getRole().equals(UserRole.PROFESSOR)){
            if (!courseRepository.existsByProfessorIdAndId(user.get().getId(), courseId)) {
                throw new StudentNotEnrolledException(user.get().getId(), courseId);
            }
        }

        return activityMapper.to(activityRepository.findByCourseIdAndActivityId(courseId, activityId));
    }
}
