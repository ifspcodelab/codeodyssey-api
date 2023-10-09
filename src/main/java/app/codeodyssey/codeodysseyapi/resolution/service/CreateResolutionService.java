package app.codeodyssey.codeodysseyapi.resolution.service;

import app.codeodyssey.codeodysseyapi.activity.data.Activity;
import app.codeodyssey.codeodysseyapi.activity.data.ActivityRepository;
import app.codeodyssey.codeodysseyapi.common.exception.*;
import app.codeodyssey.codeodysseyapi.course.data.Course;
import app.codeodyssey.codeodysseyapi.course.data.CourseRepository;
import app.codeodyssey.codeodysseyapi.resolution.api.ResolutionResponse;
import app.codeodyssey.codeodysseyapi.resolution.data.Resolution;
import app.codeodyssey.codeodysseyapi.resolution.data.ResolutionRepository;
import app.codeodyssey.codeodysseyapi.user.data.User;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CreateResolutionService {
    private final ResolutionMapper resolutionMapper;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final ActivityRepository activityRepository;
    private final ResolutionRepository resolutionRepository;

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

        if (activity.isEmpty()) {
            throw new ResourceNotFoundException(activityId, Resource.ACTIVITY);
        }

        byte[] resolutionFile = Base64.getDecoder().decode(command.resolutionFile().getBytes());

        Resolution resolution = new Resolution(activity.get(), user.get(), resolutionFile);

        if (resolution.getSubmitDate().isBefore(activity.get().getStartDate())) {
            throw new ViolationException(
                    Resource.RESOLUTION,
                    ViolationType.RESOLUTION_SUBMIT_DATE_BEFORE_ACTIVITY_STAR_DATE,
                    resolution.getSubmitDate().toString());
        }

        if (resolution.getSubmitDate().isAfter(activity.get().getEndDate())) {
            throw new ViolationException(
                    Resource.RESOLUTION,
                    ViolationType.RESOLUTION_SUBMIT_DATE_AFTER_ACTIVITY_END_DATE,
                    resolution.getSubmitDate().toString());
        }

        resolutionRepository.save(resolution);

        return resolutionMapper.to(resolution);
    }
}
