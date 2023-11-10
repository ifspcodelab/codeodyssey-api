package app.codeodyssey.codeodysseyapi.activity.service;

import app.codeodyssey.codeodysseyapi.activity.api.ActivityRequest;
import app.codeodyssey.codeodysseyapi.activity.api.ActivityResponse;
import app.codeodyssey.codeodysseyapi.activity.data.Activity;
import app.codeodyssey.codeodysseyapi.activity.data.ActivityRepository;
import app.codeodyssey.codeodysseyapi.common.exception.*;
import app.codeodyssey.codeodysseyapi.course.data.Course;
import app.codeodyssey.codeodysseyapi.course.data.CourseRepository;
import app.codeodyssey.codeodysseyapi.user.data.User;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateActivityService {

    private final ActivityRepository activityRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final ActivityMapper activityMapper;

    public ActivityResponse execute(UUID courseId, ActivityRequest request, Authentication authentication) {

        Course course = this.courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException(courseId, Resource.COURSE));

        if (request.startDate().isBefore(Instant.now().minusSeconds(86400))) {
            throw new ViolationException(
                    Resource.ACTIVITY,
                    ViolationType.ACTIVITY_START_DATE_BEFORE_TODAY,
                    request.startDate().toString());
        }

        if (request.endDate().isBefore(request.startDate())) {
            throw new ViolationException(
                    Resource.ACTIVITY,
                    ViolationType.ACTIVITY_END_DATE_BEFORE_START_DATE,
                    request.endDate().toString());
        }


        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        User user = this.userRepository.findByEmail(username)
                .orElseThrow(() -> new EmailNotFoundException(username));

        if (!course.getProfessor().getId().equals(user.getId())) {
            throw new ForbiddenAccessException(user.getId());
        }

        Activity activity = this.activityRepository.save(new Activity(
                request.title(),
                request.description(),
                request.startDate(),
                request.endDate(),
                request.initialFile(),
                request.solutionFile(),
                request.testFile(),
                request.extension(),
                course));

        return this.activityMapper.to(activity);

    }

}
