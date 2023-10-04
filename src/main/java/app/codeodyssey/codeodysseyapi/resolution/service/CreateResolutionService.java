package app.codeodyssey.codeodysseyapi.resolution.service;

import app.codeodyssey.codeodysseyapi.activity.data.Activity;
import app.codeodyssey.codeodysseyapi.activity.data.ActivityRepository;
import app.codeodyssey.codeodysseyapi.common.exception.EmailNotFoundException;
import app.codeodyssey.codeodysseyapi.common.exception.Resource;
import app.codeodyssey.codeodysseyapi.common.exception.ResourceNotFoundException;
import app.codeodyssey.codeodysseyapi.course.data.Course;
import app.codeodyssey.codeodysseyapi.course.data.CourseRepository;
import app.codeodyssey.codeodysseyapi.resolution.api.ResolutionResponse;
import app.codeodyssey.codeodysseyapi.resolution.data.Resolution;
import app.codeodyssey.codeodysseyapi.user.data.User;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CreateResolutionService {
    private final ResolutionMapper resolutionMapper;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final ActivityRepository activityRepository;

    public ResolutionResponse execute(UUID courseId, UUID activityId, CreateResolutionCommand command, String userEmail) {
        Optional<User> user = userRepository.findByEmail(userEmail);

        if (user.isEmpty()) {
            throw new EmailNotFoundException(userEmail);
        }

        Optional<Course> course = courseRepository.findById(courseId);

        if (course.isEmpty()) {
            throw new ResourceNotFoundException(courseId, Resource.COURSE);
        }

        Optional<Activity> activity = activityRepository.findById(activityId);

        Resolution resolution = new Resolution(activity.get(), user.get(), command.resolutionFile());
        return resolutionMapper.to(resolution);
    }
}
