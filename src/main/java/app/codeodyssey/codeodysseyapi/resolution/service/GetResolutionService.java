package app.codeodyssey.codeodysseyapi.resolution.service;

import app.codeodyssey.codeodysseyapi.activity.data.ActivityRepository;
import app.codeodyssey.codeodysseyapi.common.exception.*;
import app.codeodyssey.codeodysseyapi.enrollment.data.EnrollmentRepository;
import app.codeodyssey.codeodysseyapi.resolution.api.ResolutionResponse;
import app.codeodyssey.codeodysseyapi.resolution.data.Resolution;
import app.codeodyssey.codeodysseyapi.resolution.data.ResolutionRepository;
import app.codeodyssey.codeodysseyapi.user.data.User;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class GetResolutionService {
    private final ResolutionRepository resolutionRepository;
    private final UserRepository userRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final ActivityRepository activityRepository;
    private final ResolutionMapper resolutionMapper;

    public ResolutionResponse execute(UUID courseId, UUID activityId, UUID resolutionId, String userEmail) {
        Optional<User> user = userRepository.findByEmail(userEmail);

        if (user.isEmpty()) {
            throw new EmailNotFoundException(userEmail);
        }

        if (!enrollmentRepository.existsByStudentIdAndInvitation_Course_Id(user.get().getId(), courseId)) {
            throw new UserNotAssociatedException(user.get().getId(), courseId);
        }

        if (!activityRepository.existsByCourseIdAndId(courseId, activityId)) {
            throw new ViolationException(Resource.ACTIVITY, ViolationType.ACTIVITY_IS_NOT_FROM_COURSE, activityId.toString());
        }

        if (resolutionRepository.existsByActivityIdAndId(activityId, resolutionId)) {
            throw new ViolationException(Resource.RESOLUTION, ViolationType.RESOLUTION_IS_NOT_FROM_ACTIVITY, resolutionId.toString());
        }

        if (!resolutionRepository.existsByStudentIdAndId(user.get().getId(), resolutionId)) {
            throw new ViolationException(Resource.RESOLUTION, ViolationType.USER_DID_NOT_CREATE_RESOLUTION, resolutionId.toString());
        }

        Resolution resolution = resolutionRepository.findById(resolutionId)
                .orElseThrow(() -> new ResourceNotFoundException(resolutionId, Resource.RESOLUTION));

        return resolutionMapper.to(resolution);
    }
}
