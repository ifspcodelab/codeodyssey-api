package app.codeodyssey.codeodysseyapi.resolution.service;

import app.codeodyssey.codeodysseyapi.activity.data.Activity;
import app.codeodyssey.codeodysseyapi.activity.data.ActivityRepository;
import app.codeodyssey.codeodysseyapi.common.exception.*;
import app.codeodyssey.codeodysseyapi.enrollment.data.EnrollmentRepository;
import app.codeodyssey.codeodysseyapi.resolution.api.ResolutionResponse;
import app.codeodyssey.codeodysseyapi.resolution.data.Resolution;
import app.codeodyssey.codeodysseyapi.resolution.data.ResolutionRepository;
import app.codeodyssey.codeodysseyapi.resolution.data.ResolutionStatus;
import app.codeodyssey.codeodysseyapi.user.data.User;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CreateResolutionService {
    private final ResolutionMapper resolutionMapper;
    private final UserRepository userRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final ActivityRepository activityRepository;
    private final ResolutionRepository resolutionRepository;
    private final RabbitMqPublisher rabbitMqPublisher;

    public ResolutionResponse execute(UUID courseId, UUID activityId, CreateResolutionCommand command, String userEmail) {
        Optional<User> user = userRepository.findByEmail(userEmail);

        if (user.isEmpty()) {
            throw new EmailNotFoundException(userEmail);
        }

        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new ResourceNotFoundException(activityId, Resource.ACTIVITY));

        if (!activityRepository.existsByCourseIdAndId(courseId, activityId)) {
            throw new ViolationException(Resource.ACTIVITY, ViolationType.ACTIVITY_IS_NOT_FROM_COURSE, activityId.toString());
        }

        if (!enrollmentRepository.existsByStudentIdAndInvitation_Course_Id(user.get().getId(), courseId)) {
            throw new UserNotAssociatedException(user.get().getId(), courseId);
        }

        Resolution resolution = new Resolution(activity, user.get(), command.resolutionFile());

        if (resolution.getSubmitDate().isBefore(activity.getStartDate())) {
            throw new ViolationException(
                    Resource.RESOLUTION,
                    ViolationType.RESOLUTION_SUBMIT_DATE_BEFORE_ACTIVITY_STAR_DATE,
                    resolution.getSubmitDate().toString());
        }

        if (resolution.getSubmitDate().isAfter(activity.getEndDate())) {
            throw new ViolationException(
                    Resource.RESOLUTION,
                    ViolationType.RESOLUTION_SUBMIT_DATE_AFTER_ACTIVITY_END_DATE,
                    resolution.getSubmitDate().toString());
        }

        if (resolutionRepository.existsByStudentIdAndActivityIdAndStatus(user.get().getId(), activityId, ResolutionStatus.WAITING_FOR_RESULTS)){
            throw new ViolationException(
                    Resource.RESOLUTION,
                    ViolationType.STUDENT_HAS_RESOLUTION_WITH_WAITING_STATUS_IN_ACTIVITY,
                    resolution.getStatus().toString()
            );
        }

        resolutionRepository.save(resolution);

        rabbitMqPublisher.sendMessage(resolution.getId().toString());

        return resolutionMapper.to(resolution);
    }
}
